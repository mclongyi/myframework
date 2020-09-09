package com.odianyun.search.whale.api.model.selectionproduct;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fishcus on 17/2/15.
 */
@Data
@NoArgsConstructor
public class TypeOfProductFilter implements Serializable{

    private List<TypeOfProduct> typeOfProductList = new ArrayList<TypeOfProduct>(){{
        add(TypeOfProduct.COMBINE);
        add(TypeOfProduct.NORMAL);
        add(TypeOfProduct.VIRTUAL);
    }};

    public void removeType (List<TypeOfProduct> typeOfProductList) {
        this.typeOfProductList.removeAll(typeOfProductList);
    }

}
