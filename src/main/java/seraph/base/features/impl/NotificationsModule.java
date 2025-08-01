package seraph.base.features.impl;

import seraph.base.Naphthav2;
import seraph.base.Map.Gui.Category;
import seraph.base.Map.Gui.SubCategory;
import seraph.base.Map.Gui.modules.ModuleClass;
import seraph.base.Map.Gui.modules.NonToggleModule;
import seraph.base.Map.Gui.notification.notifications.NotificationStyle;
import seraph.base.Map.Gui.settings.impl.DropDown;
@ModuleClass
public class NotificationsModule extends NonToggleModule {

    private final DropDown notificationMode = new DropDown("Style","Style of notification to use",new String[]{"basic","chat"});

    public DropDown getNotificationMode(){
        return this.notificationMode;
    }

    public NotificationsModule(){
        super(
                "Notifications",
                "Configure client notifications",
                Category.TWO,
                SubCategory.GUI
        );
        this.addSettings(notificationMode);
    }

    public NotificationStyle getActiveStyle(){
        for(NotificationStyle var0 : NotificationStyle.values()){
            if(var0.key.equalsIgnoreCase(notificationMode.getValue())) return var0;
        }
        Naphthav2.sendModMsg("Error matching Notification Style");
        return NotificationStyle.CHAT;
    }
}
