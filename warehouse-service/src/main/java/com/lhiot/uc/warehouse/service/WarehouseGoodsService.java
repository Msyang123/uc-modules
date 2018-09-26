package com.lhiot.uc.warehouse.service;

import com.leon.microx.util.BeanUtils;
import com.leon.microx.util.Calculator;
import com.leon.microx.util.Pair;
import com.lhiot.uc.warehouse.domain.common.PagerResultObject;
import com.lhiot.uc.warehouse.domain.entity.WarehouseConvert;
import com.lhiot.uc.warehouse.domain.entity.WarehouseGoods;
import com.lhiot.uc.warehouse.domain.entity.WarehouseGoodsExtract;
import com.lhiot.uc.warehouse.domain.entity.WarehouseUser;
import com.lhiot.uc.warehouse.domain.enums.ConvertType;
import com.lhiot.uc.warehouse.domain.enums.InOutType;
import com.lhiot.uc.warehouse.domain.enums.OperationType;
import com.lhiot.uc.warehouse.domain.model.WarehouseGoodsParam;
import com.lhiot.uc.warehouse.mapper.WarehouseConvertMapper;
import com.lhiot.uc.warehouse.mapper.WarehouseGoodsExtractMapper;
import com.lhiot.uc.warehouse.mapper.WarehouseGoodsMapper;
import com.lhiot.uc.warehouse.mapper.WarehouseUserMapper;
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
 * @date 2018/09/07
 */
@Slf4j
@Service
@Transactional
public class WarehouseGoodsService {

    private final WarehouseUserMapper warehouseUserMapper;
    private final WarehouseGoodsMapper warehouseGoodsMapper;
    private final WarehouseGoodsExtractMapper warehouseGoodsExtractMapper;
    private final WarehouseConvertMapper warehouseConvertMapper;

    @Autowired
    public WarehouseGoodsService(WarehouseUserMapper warehouseUserMapper, WarehouseGoodsMapper warehouseGoodsMapper, WarehouseGoodsExtractMapper warehouseGoodsExtractMapper, WarehouseConvertMapper warehouseConvertMapper) {
        this.warehouseUserMapper = warehouseUserMapper;
        this.warehouseGoodsMapper = warehouseGoodsMapper;
        this.warehouseGoodsExtractMapper = warehouseGoodsExtractMapper;
        this.warehouseConvertMapper = warehouseConvertMapper;
    }

    /**
     * Description:根据id查找仓库商品
     *
     * @param id Long
     * @return WarehouseGoods
     */
    public WarehouseGoods selectById(Long id) {
        return this.warehouseGoodsMapper.selectById(id);
    }

    /**
     * Description: 查询仓库商品总记录数
     *
     * @param warehouseGoods WarehouseGoods
     * @return long
     */
    public long count(WarehouseGoods warehouseGoods) {
        return this.warehouseGoodsMapper.pageWarehouseGoodsCounts(warehouseGoods);
    }

    /**
     * Description: 查询仓库商品分页列表
     *
     * @param warehouseGoods WarehouseGoods
     * @return PagerResultObject<WarehouseGoods>
     */
    public PagerResultObject<WarehouseGoods> pageList(WarehouseGoods warehouseGoods) {
        long total = 0;
        if (warehouseGoods.getRows() != null && warehouseGoods.getRows() > 0) {
            total = this.count(warehouseGoods);
        }
        return PagerResultObject.of(warehouseGoods, total,
                this.warehouseGoodsMapper.pageWarehouseGoodss(warehouseGoods));
    }

    /**
     * 添加到仓库
     * 只有当天的水果合并在一起
     *
     * @param warehouseGoodsList List<WarehouseGoods>
     * @param baseUserId         Long
     * @param remark             String
     */
    public boolean addWarehouseGoods(List<WarehouseGoods> warehouseGoodsList, Long baseUserId, String remark) {

        //查找用户仓库
        WarehouseUser warehouseUser = warehouseUserMapper.findByBaseUserId(baseUserId);
        if (Objects.isNull(warehouseUser)) {
            return false;
        }
        //当天的仓库水果
        List<WarehouseGoods> todayWarehouseGoodsList = this.warehouseGoodsMapper.findWarehouseGoodsByWareHouseIdAndToday(warehouseUser.getId());

        //转换记录集合
        List<WarehouseConvert> warehouseConvert = new ArrayList<>();
        //更新商品集合
        List<WarehouseGoods> warehouseGoodsUpdate = new ArrayList<>();
        //插入商品集合
        List<WarehouseGoods> warehouseGoodsCreate = new ArrayList<>();
        Date current = new Date();

        final boolean isEmpty = CollectionUtils.isEmpty(todayWarehouseGoodsList);

        warehouseGoodsList.forEach(goods -> {
            goods.setWarehouseId(warehouseUser.getId());//设置仓库编号
            //重置时间的时分秒（此处用作定时任务每天刷一次）
            goods.setBuyAt(current);
            //拼接出入库记录
            WarehouseConvert wareHouseConvert = new WarehouseConvert();
            //复制属性
            BeanUtils.of(wareHouseConvert).populate(goods);
            wareHouseConvert.setConvertAt(current);// 出入库时间
            wareHouseConvert.setInOut(InOutType.IN);// 出入库标志
            wareHouseConvert.setRemark(remark);// 出入库原因
            wareHouseConvert.setConvertType(ConvertType.MANUAL);//手动
            //TODO 兑换折扣需要获取系统参数
            wareHouseConvert.setDiscount(10);//兑换折扣
            warehouseConvert.add(wareHouseConvert);
            boolean flag = false;
            //检查是否当天依据有存入水果，如果存在就合并
            for (WarehouseGoods userWarehouseGoods : todayWarehouseGoodsList) {
                if (Objects.equals(goods.getGoodsId(), userWarehouseGoods.getGoodsId())) {
                    flag = true;
                    break;
                }
            }

            //判断直接插入还是更新
            if (isEmpty || !flag) {
                warehouseGoodsCreate.add(goods);
            } else {
                //更新
                todayWarehouseGoodsList.stream().filter(userWarehouseGoods -> Objects.equals(goods.getGoodsId(), userWarehouseGoods.getGoodsId()))
                        .forEach(userWarehouseGoods -> {
                                    goods.setGoodsCount(goods.getGoodsCount().add(userWarehouseGoods.getGoodsCount()));
                                    double newValue = Calculator.mul(goods.getPrice(), goods.getGoodsCount().doubleValue());
                                    double oldValue = Calculator.mul(userWarehouseGoods.getPrice(), userWarehouseGoods.getGoodsCount().doubleValue());
                                    //计算平均价格
                                    int avgPrice = (int) Calculator.div((newValue + oldValue), (goods.getGoodsCount().doubleValue() + userWarehouseGoods.getGoodsCount().doubleValue()));
                                    goods.setPrice(avgPrice);
                                    goods.setId(userWarehouseGoods.getId());
                                    warehouseGoodsUpdate.add(goods);
                                }
                        );
            }
        });

        //批量写入用户仓库商品信息
        if (!CollectionUtils.isEmpty(warehouseGoodsCreate)) {
            warehouseGoodsMapper.batchSave(warehouseGoodsCreate);
        }
        //修改仓库商品信息
        warehouseGoodsUpdate.forEach(warehouseGoodsMapper::updateCountAndPrice);

        //批量写入仓库转换记录
        warehouseConvertMapper.saveWarehouseConvertBatch(warehouseConvert);
        return true;
    }

    /**
     * 分析要提取的仓库商品和总价格
     * 注：此处只计算，不进行数据库真实操作
     *
     * @param warehouseGoodsParamList 商品集合
     * @return 总价格、要提取的仓库商品
     */
    public Pair<Integer, List<WarehouseGoods>> waitExtractWarehouseGoods(Long baseUserId,
                                                                         List<WarehouseGoodsParam> warehouseGoodsParamList) {
        WarehouseUser warehouseUser = warehouseUserMapper.findByBaseUserId(baseUserId);
        if (Objects.isNull(warehouseUser)) {
            log.error("未找到用户仓库{}", baseUserId);
            return null;
        }
        //待提取的仓库商品列表
        List<WarehouseGoods> needFetchWarehouseGoods = new ArrayList<>();
        int totalPrice = 0;//待计算的商品总价
        //设定一个0值
        BigDecimal zero = new BigDecimal("0.000");
        for (WarehouseGoodsParam goodsParam : warehouseGoodsParamList) {
            //待提取单个商品总数
            double amount = goodsParam.getGoodsCount().doubleValue();// 现在改成直接前端计算总数//Arith.mul(goods.getAmount(),
            //当前商品提取数量为0则跳过
            if (Calculator.compareTo(amount, zero.doubleValue()) == 0) {
                log.warn("提取仓库商品要求提取数量为0{}", goodsParam);
                continue;
            }

            //检测用户仓库的商品是否存在，如果不存在直接过滤掉此商品提货
            WarehouseGoods searchWarehouseGoods = new WarehouseGoods();
            searchWarehouseGoods.setGoodsId(goodsParam.getGoodsId());
            searchWarehouseGoods.setWarehouseId(warehouseUser.getId());
            searchWarehouseGoods.setSidx("buy_at");
            searchWarehouseGoods.setSord("asc");
            //查找仓库中的指定商品信息
            List<WarehouseGoods> hasWarehouseGoodsList = warehouseGoodsMapper.pageWarehouseGoodss(searchWarehouseGoods);
            //未找到指定的商品
            if (CollectionUtils.isEmpty(hasWarehouseGoodsList)) {
                log.warn("提取仓库商品未找到指定的商品{}", searchWarehouseGoods);
                continue;
            }

            // 参数中的商品数量乘以商品单位 = 要提取的当前商品总数量
            double goodsPrice = 0;// 单个商品的总价
            for (WarehouseGoods whGoods : hasWarehouseGoodsList) {
                //如果刚好REMOVE后amount数值为0，说明已经提取足够数量的仓库商品，跳出循环
                if (Calculator.compareTo(amount, zero.doubleValue()) == 0) {
                    log.warn("刚好REMOVE后amount数值为0，说明已经提取足够数量的仓库商品，跳出循环,{},{}", amount, whGoods);
                    break;
                }
                BigDecimal whGoodsAmount = whGoods.getGoodsCount();

                if (whGoodsAmount.compareTo(zero) == 0) {
                    log.warn("数据库中存在仓库商品数量为0的记录{}", whGoods);
                    continue;
                }

                //将要提取的商品加入返回list中
                needFetchWarehouseGoods.add(whGoods);
                // 仓库商品数量/重量
                int whGoodsPrice = whGoods.getPrice(); // 仓库商品基础单位价格
                //检测是否当前记录是否够提取，如果不够就继续提取下一条
                if (Calculator.gtOrEq(amount, whGoodsAmount.doubleValue(), 4)) {
                    whGoods.setOperationType(OperationType.REMOVE);
                    int price = Calculator.toInt(Calculator.mul(whGoodsPrice, whGoodsAmount.doubleValue()));
                    goodsPrice = Calculator.add(goodsPrice, price);
                    //计算单个待提取商品均价  相同商品此属性值相同
                    //whGoods.setPrice(price);
                    //需要减掉目标要提取的数量
                    amount = Calculator.sub(amount, whGoodsAmount.doubleValue());
                } else {
                    whGoods.setOperationType(OperationType.UPDATE);
                    //价格为:仓库商品单价*剩余要提取的商品数量
                    int price = Calculator.toInt(Calculator.mul(whGoodsPrice, amount));
                    goodsPrice = Calculator.add(goodsPrice, price);
                    //计算单个待提取商品均价 每次覆盖 相同商品此属性值相同 订单商品表中price
                    //whGoods.setPrice(price);
                    //此处为要更新的仓库商品数量 更新为 goods_count=goods_count-amount
                    whGoods.setGoodsCount(new BigDecimal(amount));
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
            totalPrice = Calculator.toInt(Calculator.add(totalPrice, goodsPrice));
        }
        if (CollectionUtils.isEmpty(needFetchWarehouseGoods)) {
            log.error("获取仓库商品集合为空{}", needFetchWarehouseGoods);
            return null;
        }
        return Pair.of(totalPrice, needFetchWarehouseGoods);
    }


    /**
     * 删除或更新仓库中的商品记录
     *
     * @param warehouseGoods 需要删除或者修改的仓库商品记录
     */
    public void batchDeleteOrUpdateWarehouseGoods(List<WarehouseGoods> warehouseGoods, String orderCode, String remark) {

        //仓库提取中间表数据列表
        final List<WarehouseGoodsExtract> warehouseGoodsExtractList = new ArrayList<>();
        //仓库转换记录数据列表
        final List<WarehouseConvert> warehouseConvertList = new ArrayList<>();
        final Date current = new Date();
        //删除要全部删除的商品
        final List<Long> needRemoveIds = new ArrayList<>();
        warehouseGoods.stream()
                .filter(item -> Objects.equals(item.getOperationType(), OperationType.REMOVE))
                .forEach(item -> {
                    needRemoveIds.add(item.getId());
                    WarehouseGoodsExtract warehouseGoodsExtract = new WarehouseGoodsExtract();
                    BeanUtils.of(warehouseGoodsExtract).populate(item);
                    warehouseGoodsExtract.setOrderCode(orderCode);
                    warehouseGoodsExtractList.add(warehouseGoodsExtract);

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
            warehouseGoodsMapper.deleteByIds(needRemoveIds);
        }

        //批量更新商品数量
        warehouseGoods.stream()
                .filter(item -> Objects.equals(item.getOperationType(), OperationType.UPDATE))
                .forEach(item -> {
                    WarehouseGoodsExtract warehouseGoodsExtract = new WarehouseGoodsExtract();
                    BeanUtils.of(warehouseGoodsExtract).populate(item);
                    warehouseGoodsExtract.setOrderCode(orderCode);
                    warehouseGoodsExtractList.add(warehouseGoodsExtract);
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
                    warehouseGoodsMapper.updateGoodsCount(item);
                });
        //将出库信息添加到仓库商品中间表
        warehouseGoodsExtractMapper.batchSaveExtract(warehouseGoodsExtractList);

        //写出库记录
        warehouseConvertMapper.saveWarehouseConvertBatch(warehouseConvertList);
    }

    /**
     * 从仓库商品中间表中恢复到仓库中
     */
    public void cancelApplyWarehouseGoods(List<WarehouseGoodsExtract> cancelWarehouseGoodsExtractList, String orderCode, String backCause) {
        final List<Long> goodsExtractIds = new ArrayList<>(cancelWarehouseGoodsExtractList.size());
        final List<WarehouseGoods> warehouseGoodsList = new ArrayList<>(cancelWarehouseGoodsExtractList.size());
        //仓库转换记录数据列表
        final List<WarehouseConvert> warehouseConvertList = new ArrayList<>();
        final Date current = new Date();
        cancelWarehouseGoodsExtractList.forEach(item -> {
            goodsExtractIds.add(item.getId());

            WarehouseGoods warehouseGoods = new WarehouseGoods();
            //将中间表对象拷贝到仓库商品对象里
            BeanUtils.of(warehouseGoods).populate(item);
            warehouseGoodsList.add(warehouseGoods);

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
        warehouseGoodsExtractMapper.deleteByIds(goodsExtractIds);
        //添加到仓库商品表中
        warehouseGoodsMapper.batchSave(warehouseGoodsList);
        //写入库记录
        warehouseConvertMapper.saveWarehouseConvertBatch(warehouseConvertList);
    }

}

