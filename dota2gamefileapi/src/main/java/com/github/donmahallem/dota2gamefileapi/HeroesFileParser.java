package com.github.donmahallem.dota2gamefileapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import okio.Source;

public class HeroesFileParser extends FileParser {
    private BaseHero baseHero;
    private BaseHero.Builder currentBuilder;
    private final static String KEY_STATUS_HEALTH="StatusHealth";
    private final static String KEY_STATUS_HEALTH_REGEN="StatusHealthRegen";
    private final static String KEY_STATUS_MANA="StatusMana";
    private final static String KEY_STATUS_MANA_REGEN="StatusManaRegen";
    private final static String KEY_WORKSHOP_GUIDE_NAME="workshop_guide_name";
    private final static String KEY_ATTACK_DAMAGE_MIN="AttackDamageMin";
    private final static String KEY_ATTACK_DAMAGE_MAX="AttackDamageMax";
    private final static String KEY_ATTACK_RATE="AttackRate";
    private final static String KEY_ATTACK_ANIMATION_POINT="AttackAnimationPoint";
    private final static String KEY_ATTACK_ACQUISITION_RANGE="AttackAcquisitionRange";
    private final static String KEY_ATTACK_RANGE="AttackRange";
    private final static String KEY_HERO_GLOW_COLOR="HeroGlowColor";
    private final static String KEY_NAME_ALIASES="NameAliases";
    private final static String KEY_HERO_ID="HeroID";

    public List<BaseHero> getHeroes() {
        return mHeroes;
    }

    private List<BaseHero> mHeroes=new ArrayList<>();
    public HeroesFileParser(String inputFile) throws FileNotFoundException {
        super(inputFile);
    }

    public HeroesFileParser(File inputFile) throws FileNotFoundException {
        super(inputFile);
    }

    public HeroesFileParser(Source source) {
        super(source);
    }

    @Override
    void parseAttribute(List<String> path, String tag, String value) {
        switch (tag){
            case KEY_STATUS_HEALTH:
                this.currentBuilder.setStatusHealth(Integer.parseInt(value));
                break;
            case KEY_STATUS_HEALTH_REGEN:
                this.currentBuilder.setStatusHealthRegen(Float.parseFloat(value));
                break;
            case KEY_STATUS_MANA:
                this.currentBuilder.setStatusMana(Integer.parseInt(value));
                break;
            case KEY_STATUS_MANA_REGEN:
                this.currentBuilder.setStatusManaRegen(Float.parseFloat(value));
                break;
            case KEY_WORKSHOP_GUIDE_NAME:
                this.currentBuilder.setWorkshopGuideName(value);
                break;
            case KEY_ATTACK_DAMAGE_MIN:
                this.currentBuilder.setAttackDamageMin(Integer.parseInt(value));
                break;
            case KEY_ATTACK_DAMAGE_MAX:
                this.currentBuilder.setAttackDamageMax(Integer.parseInt(value));
                break;
            case KEY_ATTACK_RATE:
                this.currentBuilder.setAttackRate(Float.parseFloat(value));
                break;
            case KEY_ATTACK_ANIMATION_POINT:
                this.currentBuilder.setAttackAnimationPoint(Float.parseFloat(value));
                break;
            case KEY_ATTACK_ACQUISITION_RANGE:
                this.currentBuilder.setAttackAcquisitionRange(Integer.parseInt(value));
                break;
            case KEY_ATTACK_RANGE:
                this.currentBuilder.setAttackRange(Integer.parseInt(value));
                break;
            case KEY_HERO_GLOW_COLOR:
                this.currentBuilder.setHeroGlowColor(value);
                break;
            case KEY_HERO_ID:
                this.currentBuilder.setHeroID(Integer.parseInt(value));
                break;
            case KEY_NAME_ALIASES:
                this.currentBuilder.setNameAliases(value);
                break;
        }
    }

    @Override
    void startBlock(List<String> path, String name) {
        if (path.size() == 1) {
            if (name.equals("npc_dota_hero_base")) {
                currentBuilder = new BaseHero.Builder();
            } else {
                currentBuilder = baseHero.newBuilder();
            }
        }
    }

    @Override
    void endBlock(List<String> path, String name) {
        if (path.size() == 1) {
            if(name.equals("npc_dota_hero_base")){
                baseHero=currentBuilder.build();
            }else{
                currentBuilder.setNpcHeroName(name);
                this.mHeroes.add(currentBuilder.build());
            }
        }
    }
}
