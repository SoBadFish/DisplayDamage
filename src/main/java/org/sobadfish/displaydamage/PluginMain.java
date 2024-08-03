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

import java.util.Random;


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
        Entity entity = event.getEntity();
        int damage = (int) event.getDamage();
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
            displayAsParticle(damage+"",numTitle,entity.asVector3f().add(0,1.5f),entity.level,damager.getDirection(),damager.yaw,entity);
            return;
        }
        if(entity instanceof Player){
            displayAsParticle(damage+"","damage:epd",entity.asVector3f().add(0,1.5f),entity.level,entity.getDirection(),entity.yaw,entity);
        }else{
            displayAsParticle(damage+"","damage:ed",entity.asVector3f().add(0,1.5f),entity.level,entity.getDirection(),entity.yaw,entity);
        }

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityHealEvent(EntityRegainHealthEvent event) {
        Entity entity = event.getEntity();
        int heal = (int) event.getAmount();
        displayAsParticle(heal+"","damage:ph",entity.asVector3f().add(0,1.5f),entity.level,entity.getDirection(),entity.yaw,entity);
    }

    private void displayAsParticle(String damage,String numTitle, Vector3f dv3, Level level, BlockFace blockFace,double yaw,Vector3 chunk){
        BlockFace rf = blockFace.rotateY();
        float v2 = 0.25f;
        for(char c: damage.toCharArray()){
            if(c >= '0' && c <= '9'){
                Vector3f r2 = getSide(dv3,rf,v2);
                double see = (yaw + 180) * Math.PI / 180;
                r2.x = (float) (r2.x + 0.25f  * Math.cos(see));
                r2.z = (float) (r2.z + 0.25f * Math.sin(see));
                SpawnParticleEffectPacket pk = new SpawnParticleEffectPacket();
                pk.identifier = numTitle;
                pk.dimensionId = level.getDimensionData().getDimensionId();
                pk.position = r2;
                int size = c - '0' ;
                pk.molangVariablesJson = ("[{\"name\":\"variable.num\",\"value\":{\"type\":\"float\",\"value\":" +
                        size +
                        "}}]").describeConstable();
                level.addChunkPacket(chunk.getChunkX(), chunk.getChunkZ(), pk);
                v2+= 0.25f;
            }
        }

    }

    private Vector3f getSide(Vector3f v3, BlockFace face, double step) {
        return new Vector3f(v3.x + (float)(face.getXOffset() * step), v3.y + (float)(face.getYOffset() * step), v3.z + (float) (face.getZOffset() * step));
    }


}
