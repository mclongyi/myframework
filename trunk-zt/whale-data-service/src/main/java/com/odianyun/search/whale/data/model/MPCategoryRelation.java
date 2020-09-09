package com.odianyun.search.whale.data.model;

/**
 * Created by jzz on 2016/12/21.
 */
public class MPCategoryRelation {
    private Long mpId;//商品id
    private Long categoryId;//前台类目树id

    public Long getMpId() {
        return mpId;
    }

    public void setMpId(Long mpId) {
        this.mpId = mpId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "MPCategoryRelation{" +
                "mpId=" + mpId +
                ", categoryId=" + categoryId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MPCategoryRelation that = (MPCategoryRelation) o;

        if (mpId != null ? !mpId.equals(that.mpId) : that.mpId != null) return false;
        return !(categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null);

    }

    @Override
    public int hashCode() {
        int result = mpId != null ? mpId.hashCode() : 0;
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        return result;
    }
}
