package com.github.donmahallem.opendotaapi;

import java.util.Arrays;

public class Hero {
    private final int mId;
    private final String mName;
    private final String mLocalizedName;
    private final String mPrimaryAttribute;
    private final String mAttackType;
    private final String[] mRoles;
    private final int mLegs;

    public Hero(Builder builder) {
        this.mId=builder.getId();
        this.mName=builder.getName();
        this.mLocalizedName=builder.getLocalizedName();
        this.mPrimaryAttribute=builder.getPrimaryAttribute();
        this.mAttackType=builder.getAttackType();
        this.mRoles=builder.getRoles();
        this.mLegs=builder.getLegs();
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getLocalizedName() {
        return mLocalizedName;
    }

    public String getPrimaryAttribute() {
        return mPrimaryAttribute;
    }

    public String getAttackType() {
        return mAttackType;
    }

    public String[] getRoles() {
        return mRoles;
    }

    public int getLegs() {
        return mLegs;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mLocalizedName='" + mLocalizedName + '\'' +
                ", mPrimaryAttribute='" + mPrimaryAttribute + '\'' +
                ", mAttackType='" + mAttackType + '\'' +
                ", mRoles=" + Arrays.toString(mRoles) +
                ", mLegs=" + mLegs +
                '}';
    }

    public static class Builder{

        private  int mId;
        private  String mName;
        private  String mLocalizedName;
        private  String mPrimaryAttribute;
        private  String mAttackType;
        private  String[] mRoles;
        private  int mLegs;

        public int getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }

        public String getLocalizedName() {
            return mLocalizedName;
        }

        public String getPrimaryAttribute() {
            return mPrimaryAttribute;
        }

        public String getAttackType() {
            return mAttackType;
        }

        public String[] getRoles() {
            return mRoles;
        }

        public int getLegs() {
            return mLegs;
        }

        public void setId(int id) {
            mId = id;
        }

        public void setName(String name) {
            mName = name;
        }

        public void setLocalizedName(String localizedName) {
            mLocalizedName = localizedName;
        }

        public void setPrimaryAttribute(String primaryAttribute) {
            mPrimaryAttribute = primaryAttribute;
        }

        public void setAttackType(String attackType) {
            mAttackType = attackType;
        }

        public void setRoles(String[] roles) {
            mRoles = roles;
        }

        public void setLegs(int legs) {
            mLegs = legs;
        }

        public Hero build(){
            return new Hero(this);
        }
    }
}
