package com.github.donmahallem.dota2gamefileapi;


public class BaseHero {

    private final int mStatusHealth;
    private final float mStatusHealthRegen;
    private final int mStatusMana;
    private final float mStatusManaRegen;
    private final String mWorkshopGuideName;
    private final int mAttackDamageMin;
    private final int mAttackDamageMax;
    private final float mAttackRate;
    private final float mAttackAnimationPoint;
    private final int mAttackAcquisitionRange;
    private final int mAttackRange;
    private final String mNameAliases;
    private final int mHeroID;
    private final int mHeroGlowColor;

    private BaseHero(Builder builder) {
        this.mStatusHealth = builder.getStatusHealth();
        this.mStatusHealthRegen = builder.getStatusHealthRegen();
        this.mStatusMana = builder.getStatusMana();
        this.mStatusManaRegen = builder.getStatusManaRegen();
        this.mWorkshopGuideName = builder.getWorkshopGuideName();
        this.mAttackAcquisitionRange = builder.getAttackAcquisitionRange();
        this.mAttackRange = builder.getAttackRange();
        this.mAttackRate = builder.getAttackRate();
        this.mAttackDamageMin = builder.getAttackDamageMin();
        this.mAttackDamageMax = builder.getAttackDamageMax();
        this.mAttackAnimationPoint = builder.getAttackAnimationPoint();
        this.mNameAliases = builder.getNameAliases();
        this.mHeroID = builder.getHeroID();
        this.mHeroGlowColor = builder.getHeroGlowColor();
    }

    public int getStatusHealth() {
        return mStatusHealth;
    }

    public float getStatusHealthRegen() {
        return mStatusHealthRegen;
    }

    public int getStatusMana() {
        return mStatusMana;
    }

    public float getStatusManaRegen() {
        return mStatusManaRegen;
    }

    public int getAttackDamageMin() {
        return mAttackDamageMin;
    }

    public int getAttackDamageMax() {
        return mAttackDamageMax;
    }

    public float getAttackRate() {
        return mAttackRate;
    }

    public float getAttackAnimationPoint() {
        return mAttackAnimationPoint;
    }

    public int getAttackAcquisitionRange() {
        return mAttackAcquisitionRange;
    }

    public int getAttackRange() {
        return mAttackRange;
    }

    public String getNameAliases() {
        return mNameAliases;
    }

    public int getHeroID() {
        return mHeroID;
    }

    public int getHeroGlowColor() {
        return mHeroGlowColor;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public String toString() {
        return "BaseHero{" +
                "mStatusHealth=" + mStatusHealth +
                ", mStatusHealthRegen=" + mStatusHealthRegen +
                ", mStatusMana=" + mStatusMana +
                ", mStatusManaRegen=" + mStatusManaRegen +
                ", mWorkshopGuideName='" + mWorkshopGuideName + '\'' +
                ", mAttackDamageMin=" + mAttackDamageMin +
                ", mAttackDamageMax=" + mAttackDamageMax +
                ", mAttackRate=" + mAttackRate +
                ", mAttackAnimationPoint=" + mAttackAnimationPoint +
                ", mAttackAcquisitionRange=" + mAttackAcquisitionRange +
                ", mAttackRange=" + mAttackRange +
                ", mNameAliases='" + mNameAliases + '\'' +
                ", mHeroID=" + mHeroID +
                ", mHeroGlowColor=" + mHeroGlowColor +
                '}';
    }

    private String getWorkshopGuideName() {
        return this.mWorkshopGuideName;
    }

    public static class Builder {
        private int mStatusHealth;
        private float mStatusHealthRegen;
        private int mStatusMana;
        private float mStatusManaRegen;
        private String mWorkshopGuideName;
        private int mAttackDamageMin;
        private int mAttackDamageMax;
        private float mAttackRate;
        private float mAttackAnimationPoint;
        private int mAttackAcquisitionRange;
        private int mAttackRange;
        private String mNameAliases;
        private int mHeroID;
        private int mHeroGlowColor;
        private String[] mRole;
        private int[] mRoleLevels;
        private int mComplexity;

        public Builder() {

        }

        public Builder(BaseHero baseHero) {
            this.setStatusHealth(baseHero.getStatusHealth());
            this.setStatusHealthRegen(baseHero.getStatusHealthRegen());
            this.setStatusMana(baseHero.getStatusMana());
            this.setStatusManaRegen(baseHero.getStatusManaRegen());
            this.setWorkshopGuideName(baseHero.getWorkshopGuideName());
            this.setAttackDamageMin(baseHero.getAttackDamageMin());
            this.setAttackDamageMax(baseHero.getAttackDamageMax());
            this.setAttackAcquisitionRange(baseHero.getAttackAcquisitionRange());
            this.setAttackAnimationPoint(baseHero.getAttackAnimationPoint());
            this.setAttackRange(baseHero.getAttackRange());
            this.setAttackRate(baseHero.getAttackRate());
            this.setNameAliases(baseHero.getNameAliases());
            this.setHeroGlowColor(baseHero.getHeroGlowColor());
            this.setHeroID(baseHero.getHeroID());
        }

        public String[] getRole() {
            return mRole;
        }

        public Builder setRole(String[] role) {
            mRole = role;
            return this;
        }

        public int[] getRoleLevels() {
            return mRoleLevels;
        }

        public Builder setRoleLevels(int[] roleLevels) {
            mRoleLevels = roleLevels;
            return this;
        }

        public int getComplexity() {
            return mComplexity;
        }

        public Builder setComplexity(int complexity) {
            mComplexity = complexity;
            return this;
        }

        public String getNameAliases() {
            return mNameAliases;
        }

        public Builder setNameAliases(String nameAliases) {
            mNameAliases = nameAliases;
            return this;
        }

        public int getHeroID() {
            return mHeroID;
        }

        public Builder setHeroID(int heroID) {
            mHeroID = heroID;
            return this;
        }

        public int getHeroGlowColor() {
            return mHeroGlowColor;
        }

        public Builder setHeroGlowColor(int heroGlowColor) {
            mHeroGlowColor = heroGlowColor;
            return this;
        }

        public Builder setHeroGlowColor(String heroGlowColor) {
            if (heroGlowColor == null) {
                this.mHeroGlowColor = 0;
                return this;
            }
            final String[] split = heroGlowColor.split(" ");
            if (split.length != 3) {
                throw new RuntimeException("Must be 3 parts");
            }
            final int red = Integer.parseInt(split[0]);
            final int blue = Integer.parseInt(split[1]);
            final int green = Integer.parseInt(split[2]);
            this.mHeroGlowColor = red<<16+blue<<8+green;
            return this;
        }

        public int getStatusHealth() {
            return mStatusHealth;
        }

        public Builder setStatusHealth(int statusHealth) {
            mStatusHealth = statusHealth;
            return this;
        }

        public float getStatusHealthRegen() {
            return mStatusHealthRegen;
        }

        public Builder setStatusHealthRegen(float statusHealthRegen) {
            mStatusHealthRegen = statusHealthRegen;
            return this;
        }

        public int getStatusMana() {
            return mStatusMana;
        }

        public Builder setStatusMana(int statusMana) {
            mStatusMana = statusMana;
            return this;
        }

        public float getStatusManaRegen() {
            return mStatusManaRegen;
        }

        public Builder setStatusManaRegen(float statusManaRegen) {
            mStatusManaRegen = statusManaRegen;
            return this;
        }

        public BaseHero build() {
            return new BaseHero(this);
        }

        public String getWorkshopGuideName() {
            return this.mWorkshopGuideName;
        }

        public Builder setWorkshopGuideName(String name) {
            this.mWorkshopGuideName = name;
            return this;
        }

        public int getAttackDamageMin() {
            return mAttackDamageMin;
        }

        public Builder setAttackDamageMin(int damage) {
            this.mAttackDamageMin = damage;
            return this;
        }

        public int getAttackDamageMax() {
            return mAttackDamageMax;
        }

        public Builder setAttackDamageMax(int attackDamageMax) {
            mAttackDamageMax = attackDamageMax;
            return this;
        }

        public float getAttackRate() {
            return mAttackRate;
        }

        public Builder setAttackRate(float attackRate) {
            mAttackRate = attackRate;
            return this;
        }

        public float getAttackAnimationPoint() {
            return mAttackAnimationPoint;
        }

        public Builder setAttackAnimationPoint(float attackAnimationPoint) {
            mAttackAnimationPoint = attackAnimationPoint;
            return this;
        }

        public int getAttackAcquisitionRange() {
            return mAttackAcquisitionRange;
        }

        public Builder setAttackAcquisitionRange(int attackAcquisitionRange) {
            mAttackAcquisitionRange = attackAcquisitionRange;
            return this;
        }

        public int getAttackRange() {
            return mAttackRange;
        }

        public Builder setAttackRange(int attackRange) {
            mAttackRange = attackRange;
            return this;
        }
    }
}
