package org.sobadfish.displaydamage;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityRegainHealthEvent;
import cn.nukkit.plugin.PluginBase;
import org.sobadfish.displaydamage.dto.DamageTextDTO;


/**
 * @author Sobadfish
 * @date 2024/8/3
 */
public class PluginMain extends PluginBase implements Listener {

    public static PluginMain INSTANCE;

    /**
     * 可用插件控制关闭
     * */
    public boolean enable = true;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.getLogger().info("成功加载伤害显示插件");
        this.getServer().getPluginManager().registerEvents(this,this);

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event){
        if(event.isCancelled() || !enable){
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
//            DamageApi.displayAsParticle(damage,numTitle,entity.getPosition().add(0,2f));
            DamageApi.displayAsParticle(new DamageTextDTO(damage,entity.getPosition().add(0,entity.getEyeHeight()),numTitle));
            return;
        }
        if(entity instanceof Player){
            DamageApi.displayAsParticle(new DamageTextDTO(damage,entity.getPosition().add(0,entity.getEyeHeight()),"damage:epd"));
        }else{
            DamageApi.displayAsParticle(new DamageTextDTO(damage,entity.getPosition().add(0,entity.getEyeHeight()),"damage:ed"));
        }

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityHealEvent(EntityRegainHealthEvent event) {
        if(event.isCancelled()|| !enable){
            return;
        }
        Entity entity = event.getEntity();
        int heal = (int) event.getAmount();
        if(entity.getHealth() >= entity.getMaxHealth()){
            return;
        }
        DamageApi.displayAsParticle(new DamageTextDTO(heal,entity.getPosition().add(0,entity.getEyeHeight()),
                "damage:ph",0.18f,0.18f,0.2f));
    }




}
