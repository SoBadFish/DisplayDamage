package org.sobadfish.displaydamage;

import cn.nukkit.Player;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.SpawnParticleEffectPacket;

import java.util.SplittableRandom;

/**
 * 伤害显示API接口
 * @author Sobadfish
 * @date 2024/8/5
 */
public class DamageApi {

    private static final SplittableRandom RANDOM = new SplittableRandom(System.currentTimeMillis());

    public static int rand(int min, int max) {
        return min == max ? max : RANDOM.nextInt(max + 1 - min) + min;
    }

    /**
     * 生成粒子数字
     * @param damage 伤害值
     * @param numTitle 粒子材质的identifier 做拓展插件可以自行传值
     * @param position 显示伤害的位置
     * */
    public static void displayAsParticle(int damage,String numTitle, Position position){
        float vx,vy,vz;
        vx = rand(-4,4) / 10f;
        vy = rand(-4,4)/ 10f;
        vz = rand(-4,4)/ 10f;

        for(Player ckPlayer: position.level.getChunkPlayers(position.getChunkX(),position.getChunkZ()).values()){
            float v2 = 0.25f;
            Vector3f dv3 = position.asVector3f();

            for(char c: (damage+"").toCharArray()){
                if(c >= '0' && c <= '9'){
                    Vector3f r2 = getSide(dv3,ckPlayer.getDirection().rotateY(),v2);
                    SpawnParticleEffectPacket pk = new SpawnParticleEffectPacket();
                    pk.identifier = numTitle;
                    pk.dimensionId = position.level.getDimensionData().getDimensionId();
                    pk.position = r2;
                    int size = c - '0' ;
                    pk.molangVariablesJson = ("[" +
                            "{\"name\":\"variable.num\",\"value\":{\"type\":\"float\",\"value\":" +size+ "}},"
                            +
                            "{\"name\":\"variable.vx\",\"value\":{\"type\":\"float\",\"value\":" +vx+ "}},"
                            +
                            "{\"name\":\"variable.vy\",\"value\":{\"type\":\"float\",\"value\":" +vy+ "}},"
                            +
                            "{\"name\":\"variable.vz\",\"value\":{\"type\":\"float\",\"value\":" +vz+ "}}"+
                            "]").describeConstable();
                    ckPlayer.dataPacket(pk);
                    v2+= 0.25f;
                }
            }
        }

    }

    private static Vector3f getSide(Vector3f v3, BlockFace face, double step) {
        return new Vector3f(v3.x + (float)(face.getXOffset() * step), v3.y + (float)(face.getYOffset() * step), v3.z + (float) (face.getZOffset() * step));
    }



}
