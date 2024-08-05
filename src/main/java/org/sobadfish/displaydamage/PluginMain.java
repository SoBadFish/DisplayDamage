package org.sobadfish.displaydamage;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityRegainHealthEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.SpawnParticleEffectPacket;
import cn.nukkit.plugin.PluginBase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SplittableRandom;


/**
 * @author Sobadfish
 * @date 2024/8/3
 */
public class PluginMain extends PluginBase implements Listener {

    @Override
    public void onEnable() {

        this.getLogger().info("成功加载伤害显示插件 ");
        this.getServer().getPluginManager().registerEvents(this,this);

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event){
        if(event.isCancelled()){
            return;
        }
        Entity entity = event.getEntity();
        int damage = (int) event.getFinalDamage();
        if(event instanceof EntityDamageByEntityEvent){
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();

            String numTitle;
            if(damager instanceof Player && entity instanceof Player){
                numTitle = "damage:epd";
            }else if(damager instanceof Player){
                numTitle = "damage:ed";
            }else{
                numTitle = "damage:epd";
            }
            displayAsParticle(damage+"",numTitle,entity);
            return;
        }
        if(entity instanceof Player){
            displayAsParticle(damage+"","damage:epd",entity);
        }else{
            displayAsParticle(damage+"","damage:ed",entity);
        }

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityHealEvent(EntityRegainHealthEvent event) {
        if(event.isCancelled()){
            return;
        }
        Entity entity = event.getEntity();
        int heal = (int) event.getAmount();
        displayAsParticle(heal+"","damage:ph",entity);
    }

    private static final SplittableRandom RANDOM = new SplittableRandom(System.currentTimeMillis());

    public static int rand(int min, int max) {
        return min == max ? max : RANDOM.nextInt(max + 1 - min) + min;
    }

    private void displayAsParticle(String damage,String numTitle, Entity displayEntity){
        float vx,vy,vz;
        vx = rand(-4,4) / 10f;
        vy = rand(-4,4)/ 10f;
        vz = rand(-4,4)/ 10f;


        for(Player ckPlayer: displayEntity.level.getChunkPlayers(displayEntity.getChunkX(),displayEntity.getChunkZ()).values()){
            float v2 = 0.25f;
            Vector3f dv3 = displayEntity.asVector3f().add(0,2f);

            for(char c: damage.toCharArray()){
                if(c >= '0' && c <= '9'){
                    Vector3f r2 = getSide(dv3,ckPlayer.getDirection().rotateY(),v2);
                    SpawnParticleEffectPacket pk = new SpawnParticleEffectPacket();
                    pk.identifier = numTitle;
                    pk.dimensionId = displayEntity.level.getDimensionData().getDimensionId();
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

    private Vector3f getSide(Vector3f v3, BlockFace face, double step) {
        return new Vector3f(v3.x + (float)(face.getXOffset() * step), v3.y + (float)(face.getYOffset() * step), v3.z + (float) (face.getZOffset() * step));
    }



}
