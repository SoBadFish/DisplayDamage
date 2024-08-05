package org.sobadfish.displaydamage.dto;

import cn.nukkit.level.Position;

/**
 * 粒子DTO伤害类
 * @author Sobadfish
 * @date 2024/8/5
 */
public class DamageTextDTO {

    /**
     * 伤害值
     * */
    public int damage;

    /**
     * 生成位置
     * */
    public Position position;

    /**
     * 粒子ID
     * */
    public String texturesId;

    /**
     * 文字宽度
     * */
    public float textWidth;

    /**
     * 文字高度
     * */
    public float textHeight;

    /**
     * 数字间隔
     * */
    public float textWeight;

    public DamageTextDTO(int damage,Position position,String texturesId){
        this(damage,position,texturesId,0.2f,0.2f,0.25f);

    }

    public DamageTextDTO(int damage,Position position,String texturesId,float width,float height,float weight){
        this.damage = damage;
        this.position = position;
        this.texturesId = texturesId;
        this.textWidth = width;
        this.textHeight = height;
        this.textWeight = weight;

    }

    @Override
    public String toString() {
        return "DamageTextDTO{" +
                "damage=" + damage +
                ", position=" + position +
                ", texturesId='" + texturesId + '\'' +
                ", textWidth=" + textWidth +
                ", textHeight=" + textHeight +
                '}';
    }
}
