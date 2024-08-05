package org.sobadfish.displaydamage;

import cn.nukkit.Player;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.SpawnParticleEffectPacket;
import org.sobadfish.displaydamage.dto.DamageTextDTO;

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
     * @param damage 配置

     * */
    public static void displayAsParticle(DamageTextDTO damage){
        float vx,vy,vz;
        String ds =  damage.damage+"";
        vx = rand(-4,4) / 10f;
        vy = rand(-4,4)/ 10f;
        vz = rand(-4,4)/ 10f;
        Vector3f dv3 = damage.position.asVector3f();

        for(Player ckPlayer: damage.position.level.getChunkPlayers(damage.position.getChunkX(),damage.position.getChunkZ()).values()){
            float v2 = damage.textWeight;
            //向左偏移
            Vector3f left = getSide(dv3,ckPlayer.getDirection().rotateYCCW(),v2 * (float) (ds.length() / 2));

            boolean textH = true;
            for(char c:ds.toCharArray()){
                if(c >= '0' && c <= '9'){
                    Vector3f r2 = getSide(left,ckPlayer.getDirection().rotateY(),v2);
                    r2 = r2.add(0,(!textH?-0.02f:0f));
                    SpawnParticleEffectPacket pk = new SpawnParticleEffectPacket();
                    pk.identifier = damage.texturesId;
                    pk.dimensionId = damage.position.level.getDimensionData().getDimensionId();
                    pk.position = r2;
                    int size = c - '0' ;
                    pk.molangVariablesJson = ("[" +
                            "{\"name\":\"variable.num\",\"value\":{\"type\":\"float\",\"value\":" +size+ "}},"
                            +
                            "{\"name\":\"variable.vx\",\"value\":{\"type\":\"float\",\"value\":" +vx+ "}},"
                            +
                            "{\"name\":\"variable.vy\",\"value\":{\"type\":\"float\",\"value\":" +vy+ "}},"
                            +
                            "{\"name\":\"variable.vz\",\"value\":{\"type\":\"float\",\"value\":" +vz+ "}},"+

                            "{\"name\":\"variable.width\",\"value\":{\"type\":\"float\",\"value\":" +damage.textWidth+ "}},"
                            +
                            "{\"name\":\"variable.height\",\"value\":{\"type\":\"float\",\"value\":" +damage.textHeight+ "}}"+

                            "]").describeConstable();
                    ckPlayer.dataPacket(pk);
                    v2+= damage.textWeight;
                    textH = !textH;
                }
            }
        }

    }

    private static Vector3f getSide(Vector3f v3, BlockFace face, double step) {
        return new Vector3f(v3.x + (float)(face.getXOffset() * step), v3.y + (float)(face.getYOffset() * step), v3.z + (float) (face.getZOffset() * step));
    }



}
