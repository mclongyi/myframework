package com.longyi.csjl.sjmo.abstractFactory;

/**
 * @author ly
 * @description TODO
 * @date 2020/10/26 19:25
 * @throw
 */
public interface CarFactory {
    /**
     * 创建奔驰
      * @return
     */
    BenzCar getBenzCar();

    /**
     * 创建特斯拉
     * @return
     */
     TeslaCar gentTeslaCar();

}    
   