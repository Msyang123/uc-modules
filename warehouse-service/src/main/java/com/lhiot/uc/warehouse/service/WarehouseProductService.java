package com.lhiot.uc.warehouse.service;

import com.leon.microx.util.BeanUtils;
import com.leon.microx.util.Calculator;
import com.leon.microx.util.Pair;
import com.lhiot.uc.warehouse.entity.WarehouseConvert;
import com.lhiot.uc.warehouse.entity.WarehouseProduct;
import com.lhiot.uc.warehouse.entity.WarehouseProductExtract;
import com.lhiot.uc.warehouse.entity.WarehouseUser;
import com.lhiot.uc.warehouse.mapper.WarehouseConvertMapper;
import com.lhiot.uc.warehouse.mapper.WarehouseProductExtractMapper;
import com.lhiot.uc.warehouse.mapper.WarehouseProductMapper;
import com.lhiot.uc.warehouse.mapper.WarehouseUserMapper;
import com.lhiot.uc.warehouse.model.ConvertType;
import com.lhiot.uc.warehouse.model.InOutType;
import com.lhiot.uc.warehouse.model.OperationType;
import com.lhiot.uc.warehouse.model.WarehouseProductParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Description:仓库商品服务类
 *
 * @author yijun
 * 2018/09/07
 */
@Slf4j
@Service
@Transactional
public class WarehouseProductService {

    private final WarehouseUserMapper warehouseUserMapper;
    private final WarehouseProductMapper warehouseProductMapper;
    private final WarehouseProductExtractMapper warehouseProductExtractMapper;
    private final WarehouseConvertMapper warehouseConvertMapper;

    @Autowired
    public WarehouseProductService(WarehouseUserMapper warehouseUserMapper, WarehouseProductMapper warehouseProductMapper, WarehouseProductExtractMapper warehouseProductExtractMapper, WarehouseConvertMapper warehouseConvertMapper) {
        this.warehouseUserMapper = warehouseUserMapper;
        this.warehouseProductMapper = warehouseProductMapper;
        this.warehouseProductExtractMapper = warehouseProductExtractMapper;
        this.warehouseConvertMapper = warehouseConvertMapper;
    }

    /**
     * Description:根据id查找仓库商品
     *
     * @param id Long
     * @return WarehouseProduct
     */
    public WarehouseProduct selectById(Long id) {
        return this.warehouseProductMapper.selectById(id);
    }

    /**
     * 添加到仓库
     * 只有当天的水果合并在一起
     *
     * @param warehouseProductList List<WarehouseProduct>
     * @param warehouseId          Long
     * @param remark               String
     */
    public boolean addWarehouseProduct(List<WarehouseProduct> warehouseProductList, Long warehouseId, String remark) {

        //当天的仓库水果
        List<WarehouseProduct> todayWarehouseProductList = this.warehouseProductMapper.findWarehouseProductByWareHouseIdAndToday(warehouseId);

        //转换记录集合
        List<WarehouseConvert> warehouseConvert = new ArrayList<>();
        //更新商品集合
        List<WarehouseProduct> warehouseProductUpdate = new ArrayList<>();
        //插入商品集合
        List<WarehouseProduct> warehouseProductCreate = new ArrayList<>();
        Date current = new Date();

        final boolean isEmpty = CollectionUtils.isEmpty(todayWarehouseProductList);

        warehouseProductList.forEach(product -> {
            //重置时间的时分秒（此处用作定时任务每天刷一次）
            product.setBuyAt(current);
            //拼接出入库记录
            WarehouseConvert wareHouseConvert = new WarehouseConvert();
            //复制属性
            BeanUtils.of(wareHouseConvert).populate(product);
            wareHouseConvert.setConvertAt(current);// 出入库时间
            wareHouseConvert.setInOut(InOutType.IN);// 出入库标志
            wareHouseConvert.setRemark(remark);// 出入库原因
            warehouseConvert.add(wareHouseConvert);
            boolean flag = false;
            //检查是否当天依据有存入水果，如果存在就合并
            for (WarehouseProduct userWarehouseProduct : todayWarehouseProductList) {
                if (Objects.equals(product.getProductId(), userWarehouseProduct.getProductId())) {
                    flag = true;
                    break;
                }
            }

            //判断直接插入还是更新
            if (isEmpty || !flag) {
                warehouseProductCreate.add(product);
            } else {
                //更新
                todayWarehouseProductList.stream().filter(userWarehouseProduct -> Objects.equals(product.getProductId(), userWarehouseProduct.getProductId()))
                        .forEach(userWarehouseProduct -> {
                                    product.setProductCount(product.getProductCount().add(userWarehouseProduct.getProductCount()));
                                    double newValue = Calculator.mul(product.getPrice(), product.getProductCount().doubleValue());
                                    double oldValue = Calculator.mul(userWarehouseProduct.getPrice(), userWarehouseProduct.getProductCount().doubleValue());
                                    //计算平均价格
                                    int avgPrice = (int) Calculator.div((newValue + oldValue), (product.getProductCount().doubleValue() + userWarehouseProduct.getProductCount().doubleValue()));
                                    product.setPrice(avgPrice);
                                    product.setId(userWarehouseProduct.getId());
                                    warehouseProductUpdate.add(product);
                                }
                        );
            }
        });

        //批量写入用户仓库商品信息
        if (!CollectionUtils.isEmpty(warehouseProductCreate)) {
            warehouseProductMapper.batchSave(warehouseProductCreate);
        }
        //修改仓库商品信息
        warehouseProductUpdate.forEach(warehouseProductMapper::updateCountAndPrice);

        //批量写入仓库转换记录
        warehouseConvertMapper.saveWarehouseConvertBatch(warehouseConvert);
        return true;
    }

    /**
     * 分析要提取的仓库商品和总价格
     * 注：此处只计算，不进行数据库真实操作
     *
     * @param warehouseProductParamList 商品集合
     * @return 总价格、要提取的仓库商品
     */
    public Pair<Integer, List<WarehouseProduct>> waitExtractWarehouseProduct(Long warehouseId,
                                                                             List<WarehouseProductParam> warehouseProductParamList) {
        WarehouseUser warehouseUser = warehouseUserMapper.selectById(warehouseId);
        if (Objects.isNull(warehouseUser)) {
            log.error("未找到用户仓库{}", warehouseId);
            return null;
        }
        //待提取的仓库商品列表
        List<WarehouseProduct> needFetchWarehouseProduct = new ArrayList<>();
        int totalPrice = 0;//待计算的商品总价
        //设定一个0值
        BigDecimal zero = new BigDecimal("0.000");
        for (WarehouseProductParam productParam : warehouseProductParamList) {
            //待提取单个商品总数
            double amount = productParam.getProductCount().doubleValue();// 现在改成直接前端计算总数//Arith.mul(product.getAmount(),
            //当前商品提取数量为0则跳过
            if (Calculator.compareTo(amount, zero.doubleValue()) == 0) {
                log.warn("提取仓库商品要求提取数量为0{}", productParam);
                continue;
            }

            //检测用户仓库的商品是否存在，如果不存在直接过滤掉此商品提货
            WarehouseProduct searchWarehouseProduct = new WarehouseProduct();
            searchWarehouseProduct.setProductId(productParam.getProductId());
            searchWarehouseProduct.setWarehouseId(warehouseUser.getId());
            //查找仓库中的指定商品信息
            List<WarehouseProduct> hasWarehouseProductList = warehouseProductMapper.pageWarehouseProducts(searchWarehouseProduct);
            //未找到指定的商品
            if (CollectionUtils.isEmpty(hasWarehouseProductList)) {
                log.warn("提取仓库商品未找到指定的商品{}", searchWarehouseProduct);
                continue;
            }

            // 参数中的商品数量乘以商品单位 = 要提取的当前商品总数量
            double productPrice = 0;// 单个商品的总价
            for (WarehouseProduct whProduct : hasWarehouseProductList) {
                //如果刚好REMOVE后amount数值为0，说明已经提取足够数量的仓库商品，跳出循环
                if (Calculator.compareTo(amount, zero.doubleValue()) == 0) {
                    log.warn("刚好REMOVE后amount数值为0，说明已经提取足够数量的仓库商品，跳出循环,{},{}", amount, whProduct);
                    break;
                }
                BigDecimal whProductAmount = whProduct.getProductCount();

                if (whProductAmount.compareTo(zero) == 0) {
                    log.warn("数据库中存在仓库商品数量为0的记录{}", whProduct);
                    continue;
                }

                //将要提取的商品加入返回list中
                needFetchWarehouseProduct.add(whProduct);
                // 仓库商品数量/重量
                int whProductPrice = whProduct.getPrice(); // 仓库商品基础单位价格
                //检测是否当前记录是否够提取，如果不够就继续提取下一条
                if (Calculator.gtOrEq(amount, whProductAmount.doubleValue(), 4)) {
                    whProduct.setOperationType(OperationType.REMOVE);
                    int price = Calculator.toInt(Calculator.mul(whProductPrice, whProductAmount.doubleValue()));
                    productPrice = Calculator.add(productPrice, price);
                    //计算单个待提取商品均价  相同商品此属性值相同
                    //whProduct.setPrice(price);
                    //需要减掉目标要提取的数量
                    amount = Calculator.sub(amount, whProductAmount.doubleValue());
                } else {
                    whProduct.setOperationType(OperationType.UPDATE);
                    //价格为:仓库商品单价*剩余要提取的商品数量
                    int price = Calculator.toInt(Calculator.mul(whProductPrice, amount));
                    productPrice = Calculator.add(productPrice, price);
                    //计算单个待提取商品均价 每次覆盖 相同商品此属性值相同 订单商品表中price
                    //whProduct.setPrice(price);
                    //此处为要更新的仓库商品数量 更新为 product_count=product_count-amount
                    whProduct.setProductCount(BigDecimal.valueOf(amount));
                    amount = 0;
                    break;
                }
            }
            //当前商品检测到待提取数量大于仓库实际数量时，提示提取失败
            if (Calculator.gt(amount, 0)) {
                log.error("当前商品检测到待提取数量大于仓库实际数量，提取失败，剩余待提取数量{}", amount);
                return null;
            }
            //累加总价
            totalPrice = Calculator.toInt(Calculator.add(totalPrice, productPrice));
        }
        if (CollectionUtils.isEmpty(needFetchWarehouseProduct)) {
            log.error("获取仓库商品集合为空{}", needFetchWarehouseProduct);
            return null;
        }
        return Pair.of(totalPrice, needFetchWarehouseProduct);
    }


    /**
     * 删除或更新仓库中的商品记录
     *
     * @param warehouseProduct 需要删除或者修改的仓库商品记录
     */
    public void batchDeleteOrUpdateWarehouseProduct(List<WarehouseProduct> warehouseProduct, String orderCode, String remark, boolean direct) {

        //仓库提取中间表数据列表
        final List<WarehouseProductExtract> warehouseProductExtractList = new ArrayList<>();
        //仓库转换记录数据列表
        final List<WarehouseConvert> warehouseConvertList = new ArrayList<>();
        final Date current = new Date();
        //删除要全部删除的商品
        final List<Long> needRemoveIds = new ArrayList<>();
        warehouseProduct.stream()
                .filter(item -> Objects.equals(item.getOperationType(), OperationType.REMOVE))
                .forEach(item -> {
                    needRemoveIds.add(item.getId());
                    WarehouseProductExtract warehouseProductExtract = new WarehouseProductExtract();
                    BeanUtils.of(warehouseProductExtract).populate(item);
                    warehouseProductExtract.setOrderCode(orderCode);
                    warehouseProductExtractList.add(warehouseProductExtract);

                    //仓库转换记录对象
                    WarehouseConvert wareHouseConvert = new WarehouseConvert();
                    //复制属性
                    BeanUtils.of(wareHouseConvert).populate(item);
                    wareHouseConvert.setConvertAt(current);// 出入库时间
                    wareHouseConvert.setInOut(InOutType.OUT);// 出入库标志
                    wareHouseConvert.setRemark(remark);// 出入库原因
                    wareHouseConvert.setConvertType(ConvertType.MANUAL);//手动
                    //TODO 兑换折扣需要获取系统参数
                    wareHouseConvert.setDiscount(10);//兑换折扣
                    warehouseConvertList.add(wareHouseConvert);
                });
        //删除掉要扣除的商品
        if (!CollectionUtils.isEmpty(needRemoveIds)) {
            warehouseProductMapper.deleteByIds(needRemoveIds);
        }

        //批量更新商品数量
        warehouseProduct.stream()
                .filter(item -> Objects.equals(item.getOperationType(), OperationType.UPDATE))
                .forEach(item -> {
                    WarehouseProductExtract warehouseProductExtract = new WarehouseProductExtract();
                    BeanUtils.of(warehouseProductExtract).populate(item);
                    warehouseProductExtract.setOrderCode(orderCode);
                    warehouseProductExtractList.add(warehouseProductExtract);
                    //仓库转换记录对象
                    WarehouseConvert wareHouseConvert = new WarehouseConvert();
                    //复制属性
                    BeanUtils.of(wareHouseConvert).populate(item);
                    wareHouseConvert.setConvertAt(current);// 出入库时间
                    wareHouseConvert.setInOut(InOutType.OUT);// 出入库标志
                    wareHouseConvert.setRemark(remark);// 出入库原因
                    wareHouseConvert.setConvertType(ConvertType.MANUAL);//手动
                    //TODO 兑换折扣需要获取系统参数
                    wareHouseConvert.setDiscount(10);//兑换折扣
                    warehouseConvertList.add(wareHouseConvert);
                    //更新扣除仓库商品
                    warehouseProductMapper.updateProductCount(item);
                });
        //如果是非直接删除，将出库信息添加到仓库商品中间表，否则直接移除仓库中的商品
        if (!direct) {
            warehouseProductExtractMapper.batchSaveExtract(warehouseProductExtractList);
        }
        //写出库记录
        warehouseConvertMapper.saveWarehouseConvertBatch(warehouseConvertList);
    }

    /**
     * 从仓库商品中间表中恢复到仓库中
     */
    public void cancelApplyWarehouseProduct(List<WarehouseProductExtract> cancelWarehouseProductExtractList, String orderCode, String backCause) {
        final List<Long> productExtractIds = new ArrayList<>(cancelWarehouseProductExtractList.size());
        final List<WarehouseProduct> warehouseProductList = new ArrayList<>(cancelWarehouseProductExtractList.size());
        //仓库转换记录数据列表
        final List<WarehouseConvert> warehouseConvertList = new ArrayList<>();
        final Date current = new Date();
        cancelWarehouseProductExtractList.forEach(item -> {
            productExtractIds.add(item.getId());

            WarehouseProduct warehouseProduct = new WarehouseProduct();
            //将中间表对象拷贝到仓库商品对象里
            BeanUtils.of(warehouseProduct).populate(item);
            warehouseProductList.add(warehouseProduct);

            //仓库转换记录对象
            WarehouseConvert wareHouseConvert = new WarehouseConvert();
            //复制属性
            BeanUtils.of(wareHouseConvert).populate(item);
            wareHouseConvert.setConvertAt(current);// 出入库时间
            wareHouseConvert.setInOut(InOutType.IN);// 出入库标志
            wareHouseConvert.setRemark(backCause);// 出入库原因
            wareHouseConvert.setConvertType(ConvertType.MANUAL);//手动
            //TODO 兑换折扣需要获取系统参数
            wareHouseConvert.setDiscount(10);//兑换折扣
            warehouseConvertList.add(wareHouseConvert);
        });
        //移除中间表数据
        warehouseProductExtractMapper.deleteByIds(productExtractIds);
        //添加到仓库商品表中
        warehouseProductMapper.batchSave(warehouseProductList);
        //写入库记录
        warehouseConvertMapper.saveWarehouseConvertBatch(warehouseConvertList);
    }

}

