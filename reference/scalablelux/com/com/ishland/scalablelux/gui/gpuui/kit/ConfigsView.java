/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_310
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.config.ClientPrefs;
import com.ishland.scalablelux.config.ConfigManager;
import com.ishland.scalablelux.config.ConfigTags;
import com.ishland.scalablelux.config.ServerAutoConfig;
import com.ishland.scalablelux.config.hub.ConfigHubManager;
import com.ishland.scalablelux.config.hub.PrivateConfigManager;
import com.ishland.scalablelux.config.hub.RemoteConfig;
import com.ishland.scalablelux.config.maker.ConfigMaker;
import com.ishland.scalablelux.config.maker.ConfigMakerEngine;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.framework.UiSmoothScroll;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.gui.gpuui.kit.UiTextField;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFontMetrics;
import com.ishland.scalablelux.util.CryptoVault;
import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;

@Environment(value=EnvType.CLIENT)
public final class ConfigsView {
    private final Backend backend;
    private final ConfigHubManager manager = new ConfigHubManager();
    private Tab tab = Tab.MINE;
    private final UiTextField saveName = new UiTextField(ConfigsView.nw("\ud31c\u4b64\u5db2\u6928\u927c\u6b1b\u2a6f\u10e9\ud248\uabf5\u3809\u9b9f\u3646\uf1b7\uaa6c", 898461229, 0x5BC794E1 ^ CryptoVault.mask())).maxLength(32).allow(UiTextField::isNameChar);
    private final UiTextField renameField = new UiTextField(ConfigsView.nw("\ub4c2\u02ea\ude02\u7780\u8528\u8bb3\u61da\u1fe4\u9822\uf628\ue50d", 1940987845, 0x5BC794E1 ^ CryptoVault.mask())).maxLength(32).allow(UiTextField::isNameChar);
    private final UiTextField hubSearch = new UiTextField(ConfigsView.nw("\u2d7b\u1fad\uf7ee\ubc84\u3891\uf5cb\ua25b\u5901\u3753\ub6db\uc60e\u0752\u4e8b\ub16e\u9845\ub80e\ucab8\u59e9\u3d85\u547e\u1e94\u94c4\u8323\u5500\u221c\u92b1\u0590\uea7b\u0b3c\u4b9a\u8d4f\ue744\u97cb\uf3b5\ud8d2\u4e9e\ua72b\u1e84\uac32\u3ba0\u19e6\u8546\ud872\uda87\ua404\u46d3\uc72d\uf993\ud521\uddcc", -284517874, 0x5BC794E1 ^ CryptoVault.mask())).maxLength(40);
    private final UiTextField shareDescription = new UiTextField(ConfigsView.nw("\u046d\u3a36\u6d54\u9111\u5ce2\u07dd\u8777\uf0c2\ubf6e\ua0c1\udf01\u785f\ud665\u9f02\uf638\ub1b9\u6420\u8a71\ud57f\u952b\u36dc\u56ad\u959e\u27eb", -479095099, 0x5BC794E1 ^ CryptoVault.mask())).maxLength(120);
    private final UiTextField importCode = new UiTextField(ConfigsView.nw("\u9c70\u7fbe\u5ad1\u0e18\u3025\ua143\u889b\u744c\u6359\ucfed\u5de9\udf47\ud6c9\u3303\ua42c\u11e0\u8939\ub0db\u11a1\u244e\uaf22\u2d47", -1385565293, 0x5BC794E1 ^ CryptoVault.mask())).maxLength(64).allow(n2 -> n2 >= 97 && n2 <= 122 || n2 >= 65 && n2 <= 90 || n2 >= 48 && n2 <= 57 || n2 == 45 || n2 == 95);
    private final UiSmoothScroll mineScroll = new UiSmoothScroll();
    private final UiSmoothScroll hubScroll = new UiSmoothScroll();
    private boolean newPublic;
    private final Set<String> newTags = new LinkedHashSet<String>();
    private String editName;
    private String editTagsFor;
    private final Set<String> editTags = new LinkedHashSet<String>();
    private String armedDeleteName;
    private final Set<String> filterTags = new LinkedHashSet<String>();
    private static final String[] SORT_LABELS = new String[]{ConfigsView.nw("\u270d\ua9d4\u2e48\u3f8a\u13f3\uce25\uc0bf\ua147\u29d1\u7e2e\ud2db\u5193\u312b\u41cb\ucff1\u0f56\uae31\u0eac", 248432717, 0x5BC794E1 ^ CryptoVault.mask()), ConfigsView.nw("\ucc20\u8e7c\u83ca\ud01c\u1fa3\u583a\uabf3\ua0bf\uf679\udba2", 1329243481, 0x5BC794E1 ^ CryptoVault.mask()), ConfigsView.nw("\ua79a\u681e\u0a8e\u9dd9\u8022\u0b6b\uef69", -1691215772, 0x5BC794E1 ^ CryptoVault.mask())};
    private static final String[] SORT_KEYS = new String[]{ConfigsView.nw("\u91c7\u450b\u0d6b\uc4e0\u349f\u80f1\u91f2\ua548\u9251\u56ee", 1322278972, 0x5BC794E1 ^ CryptoVault.mask()), ConfigsView.nw("\ub460\u85e9\u7285\uc902", 1817440713, 0x5BC794E1 ^ CryptoVault.mask()), ConfigsView.nw("\u6197\u04d3\u29c0\ue1c9\u3769\u2e10\u19f3\ubb28\u0a58", -1052247277, 0x5BC794E1 ^ CryptoVault.mask())};
    private int sortIndex;
    private String lastUsernameSearch;
    private boolean rating;
    private static final int STAR_ON = -340971;
    private boolean keepOriginalKeybinds;
    private List<RemoteConfig> hub = List.of();
    private boolean hubLoaded;
    private boolean hubLoading;
    private String hubError;
    private RemoteConfig selected;
    private int shareIndex;
    private final float[] mineRect = new float[4];
    private final float[] hubRect = new float[4];
    private float lastScale = 1.0f;
    private String armedDeleteId;
    private long armedAt;
    private boolean makerOpen;
    private int makerStep;
    private List<ConfigMaker.Question> makerQuestions = List.of();
    private final Map<String, String> makerAnswers = new HashMap<String, String>();
    private ConfigMakerEngine.Plan makerPlan;
    private List<ConfigMaker.PreviewModule> makerPreview = List.of();
    private boolean makerSaving;
    private final UiTextField makerName = new UiTextField(ConfigsView.nw("\u231a\u10c8\u375a\ua7b4\u9421\u05b7\u8c8b\u6193\u946a\u8aa6\ue9f3\ucbb7\u3c39", -1293851558, 0x5BC794E1 ^ CryptoVault.mask())).maxLength(32).allow(UiTextField::isNameChar);
    private final UiSmoothScroll makerScroll = new UiSmoothScroll();
    private final float[] makerRect = new float[4];
    private String menuName;
    private Info menuInfo;
    private float menuX;
    private float menuY;
    private float lastMx;
    private float lastMy;
    private String confirmDeleteName;
    private Info confirmDeleteInfo;

    public ConfigsView(Backend backend) {
        this.backend = backend;
        this.makerName.onSubmit(() -> this.saveMaker(false));
        this.saveName.onSubmit(this::saveCurrent);
        this.hubSearch.onSubmit(this::searchByUsername);
        this.importCode.onSubmit(this::importShareCode);
    }

    public void showCommunity(List<RemoteConfig> list, RemoteConfig remoteConfig) {
        this.tab = Tab.COMMUNITY;
        this.hub = List.copyOf(list);
        this.hubLoaded = true;
        this.selected = remoteConfig;
    }

    public void refresh() {
        this.refresh(null);
    }

    private void refresh(String string2) {
        if (this.hubLoading) {
            return;
        }
        this.hubLoading = true;
        this.hubError = null;
        this.lastUsernameSearch = string2;
        int n2 = this.sortIndex;
        this.manager.fetchConfigs(string2, SORT_KEYS[n2], list -> {
            this.hub = list;
            this.hubLoading = false;
            this.hubLoaded = true;
            if (this.selected != null) {
                String string = this.selected.id;
                this.selected = this.hub.stream().filter(remoteConfig -> remoteConfig.id != null && remoteConfig.id.equals(string)).findFirst().orElse(null);
            }
            if (n2 != this.sortIndex) {
                this.refresh(this.lastUsernameSearch);
            }
        }, string -> {
            this.hubLoading = false;
            this.hubLoaded = true;
            this.hubError = string;
        });
    }

    private void searchByUsername() {
        String string = this.hubSearch.value().trim();
        if (string.isEmpty()) {
            this.refresh();
            return;
        }
        this.refresh(string);
    }

    private void saveCurrent() {
        String string = this.saveName.value().trim();
        if (string.isEmpty()) {
            this.backend.setStatus(ConfigsView.nw("\u36e9\u291b\ua809\ub534\u5ed2\u92ce\u20c7\u470d\u126e\u54b7\ua899\udc01\uaa4c\u99b6\ueaef\ub534\u1309\u9b29\ue700\u7c4c\u16a5\u81ed\u040a\u215a\uc8ba\u98a8\ud578\u0d44\u8286\u4578\u4614\ua93c\uac51\ud11e\u9c51\u66e5\ue8ae", -1157189288, 0x5BC794E1 ^ CryptoVault.mask()));
            return;
        }
        this.backend.save(string, this.newPublic, new ArrayList<String>(this.newTags));
        this.saveName.setValue("");
    }

    public boolean scroll(double d2, double d3, double d4, long l2) {
        float[] fArray;
        boolean bl;
        boolean bl2 = bl = this.makerOpen && this.tab == Tab.MINE;
        float[] fArray2 = bl ? this.makerRect : (fArray = this.tab == Tab.MINE ? this.mineRect : this.hubRect);
        if (d2 < (double)fArray[0] || d2 > (double)(fArray[0] + fArray[2]) || d3 < (double)fArray[1] || d3 > (double)(fArray[1] + fArray[3])) {
            return false;
        }
        (bl ? this.makerScroll : (this.tab == Tab.MINE ? this.mineScroll : this.hubScroll)).wheel(d4, 56.0f * this.lastScale, l2);
        return true;
    }

    public void draw(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.lastScale = f6;
        this.lastMx = f7;
        this.lastMy = f8;
        float f9 = Math.round(22.0f * f6);
        UiKit.title(builder, uiHost, ConfigsView.nw("\uf06f\udf29\ufd24\u82bf\ufb88\ue1df\uc617\uc030\ubbb1\udd19\u841e", 1255541678, 0x5BC794E1 ^ CryptoVault.mask()), f2 + f9, f3 + (float)Math.round(20.0f * f6), f6);
        float f10 = Math.round(220.0f * f6);
        float f11 = Math.round(30.0f * f6);
        UiKit.segmented(builder, uiHost, new String[]{ConfigsView.nw("\u8b9c\uecc6\u0f18\u5c03\u70a1\ub15d\u7119\ub6b8\u081d\u66e6\u7034\u30ef\uc52f\ua568", 952941466, 0x5BC794E1 ^ CryptoVault.mask()), ConfigsView.nw("\ufa9c\u1f1b\u31a9\uda8e\u6fbe\u1c2c\uac87\u4111\uce25\ua25b", 1359593368, 0x5BC794E1 ^ CryptoVault.mask())}, this.tab.ordinal(), f2 + f4 - f9 - f10, f3 + (float)Math.round(14.0f * f6), f10, f11, f6, f7, f8, n2 -> {
            this.tab = Tab.values()[n2];
            uiHost.focus(null);
        });
        this.drawMakerButton(builder, uiHost, f2 + f4 - f9 - f10 - (float)Math.round(10.0f * f6), f3 + (float)Math.round(14.0f * f6), f11, f6, f7, f8);
        UiKit.divider(builder, uiHost, f2 + f9, f3 + (float)Math.round(56.0f * f6), f4 - f9 * 2.0f);
        float f12 = f3 + (float)Math.round(72.0f * f6);
        float f13 = Math.round(36.0f * f6);
        float f14 = f3 + f5 - f13;
        float f15 = f2 + f9;
        float f16 = f4 - f9 * 2.0f;
        if (this.tab == Tab.MINE && this.makerOpen) {
            this.drawMaker(builder, uiHost, f15, f12, f16, f14 - f12, f6, f7, f8);
        } else if (this.tab == Tab.MINE) {
            this.drawMine(builder, uiHost, f15, f12, f16, f14 - f12, f6, f7, f8);
        } else {
            this.drawCommunity(builder, uiHost, f15, f12, f16, f14 - f12, f6, f7, f8);
        }
        UiKit.divider(builder, uiHost, f2 + f9, f14 + (float)Math.round(4.0f * f6), f4 - f9 * 2.0f);
        String string = this.backend.status();
        if (string != null && !string.isEmpty()) {
            builder.text(f15, f14 + (float)Math.round(14.0f * f6), 10.0f * f6, uiHost.accent(), UiKit.fit(string, f16, 10.0f * f6));
        } else if (!this.backend.signedIn()) {
            builder.text(f15, f14 + (float)Math.round(14.0f * f6), 10.0f * f6, uiHost.muted2Color(), ConfigsView.nw("\uc62c\u858d\u03b2\u6385\u82fb\u81d6\u67cf\u9ce6\ue2d8\ub95e\u5c10\ua4e7\uca97\ueb8a\u4a37\ue035\uddfc\u5cc5\u9542\u2be4\u3ebd\uc7a8\u1d7b\uae7f\u3413\ua347\u01e9\u1431\u78c3\u2ad4\uf331\uafb4\uebe3\u4fe4\uc83d\ufe77\u2140\u5463\ua510\ud6a3\uaa45\u8967\u2fd5\u3579\ue980\u36ea\u1837\u2527\u10cb\u3489\ufaa3\uc2cf\uc847\u6d4a\ub8a4\u2cbd\ub8bd\ueb77\ud9bc\u140a\u2907\ud9d2\u178f\u7c75\u836c\u3020\u159f\uaac3\ucb2b\uae4a\u8f2e\u663b\ub6be\udfa8\u9b4b\u6c8c", -1005990200, 0x5BC794E1 ^ CryptoVault.mask()));
        } else if (this.tab == Tab.MINE && !this.makerOpen) {
            builder.text(f15, f14 + (float)Math.round(14.0f * f6), 10.0f * f6, uiHost.muted2Color(), ConfigsView.nw("\u15e6\ude5c\ue5ca\uff85\u4659\ue05a\ua374\ud937\u7617\u6a2c\u1e65\u6a1c\u3eee\u8046\uebf0\uaa31\u3b2e\u3c5f\uc48c\ua1b6\u6d77\u944f\u2520\ua7c1\u5ae3\u1c3b\u20b3\u3d7a\u52a1\u8fa8\uea15\ub0e2\uaa5b\ucff7\u9745\u7ed3\u5e93\u3b8e\ua322\uae8f\u4826\ud7df\ue9c9\ud79b\u3b5b\u49f6\u63b8\u68d0\u3f38\u34e0\u7350\u4450\uc7d6\u35bd\u97ee\ub4e2\ue05a\u2ef7\u1cbb\u6b82\u1d9d\u6d29\u3c2e\u10a9\u94c8\u8f30\u869f\u6d01\u71b6\u268e\u2dd4\u54f2", 541151297, 0x5BC794E1 ^ CryptoVault.mask()));
        }
        if (this.tab == Tab.MINE && !this.makerOpen) {
            this.drawOverlays(builder, uiHost, f2, f3, f4, f5, f6, f7, f8);
        } else {
            this.menuName = null;
            this.confirmDeleteName = null;
        }
    }

    private void drawMakerButton(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7) {
        if (!this.backend.premium()) {
            this.makerOpen = false;
            return;
        }
        String string = ConfigsView.nw("\u8504\u22ba\udbe2\udf5a\u720e\u09f0\ufef0\u55ce\u19dc\uedd8\u88e8\ua701\u0c4c\u276e", -1874657017, 0x5BC794E1 ^ CryptoVault.mask());
        float f8 = UiKit.buttonWidth(string, f5);
        float f9 = f2 - f8;
        boolean bl = this.makerOpen && this.tab == Tab.MINE;
        UiKit.button(builder, uiHost, string, f9, f3, f8, f4, f5, bl ? UiKit.Style.PRIMARY : UiKit.Style.SECONDARY, true, f6, f7, this::toggleMaker);
    }

    private void toggleMaker() {
        if (!this.backend.premium()) {
            return;
        }
        if (this.makerOpen && this.tab == Tab.MINE) {
            this.makerOpen = false;
            return;
        }
        this.tab = Tab.MINE;
        if (this.makerOpen) {
            return;
        }
        this.makerQuestions = ConfigMaker.questions();
        if (this.makerQuestions.isEmpty()) {
            this.backend.setStatus(ConfigsView.nw("\ubf7f\u9af2\uaa2c\u8453\u3585\ucdc6\udfde\uc6d0\u797d\u92a7\u23d1\u4fdd\u0fa1\u4573\u88f1\ua82f\u891d\u0497\u24eb\u5ba7\u5edd\u9b14\uf58e\u71db\u82bb\u6d80\u0271\u644d\u5ac2\ue1e8\uf5dd\u1ea5\u11d6\u881a\u863f\u0969\uaa07\ue7cd\uc925\u07e0\u73c0\ud271\u4599\u81f0\ua375\ub81f\ufb3e\uc683\ued0c\u7be1\u129d\u4a01\u2233\uc961\uf05d", 521737966, 0x5BC794E1 ^ CryptoVault.mask()));
            return;
        }
        this.makerOpen = true;
        this.makerStep = 0;
        this.makerAnswers.clear();
        this.makerPlan = null;
        this.makerPreview = List.of();
        this.makerSaving = false;
    }

    private void makerNext() {
        if (this.makerStep < this.makerQuestions.size() - 1) {
            ++this.makerStep;
            return;
        }
        this.makerPlan = ConfigMaker.plan(this.makerAnswers);
        if (this.makerPlan == null) {
            this.backend.setStatus(ConfigsView.nw("\ub93d\ud194\u298e\u2bd9\u342d\ud836\ue1ac\u8f49\ue034\u882e\u4225\ua046\u4e2e\u73cb\uc697\u0597\u5eb6\uf9f3\u611a\u18e7\u2810\ua70a\uefed\u623f\uf800\uc588\u0491\u2f7c\u8af7\u603c\u1f90\ua75e\u50e7\ue13a\uee98\u88df\u28e7\u9905\ud3b6\ufa10\u2999\ueffb\u06d2\u6da6\u16f2", -955103969, 0x5BC794E1 ^ CryptoVault.mask()));
            return;
        }
        this.makerPreview = ConfigMaker.preview(this.makerPlan);
        this.makerName.setValue(this.makerPlan.name);
        this.makerStep = this.makerQuestions.size();
    }

    private void saveMaker(boolean bl2) {
        if (this.makerSaving || this.makerPlan == null) {
            return;
        }
        if (!this.backend.premium()) {
            this.backend.setStatus(ConfigsView.nw("\u8750\u232b\uee97\u8867\u943e\u3e1c\u8701\u7648\u2e31\u0b85\uc5fb\ua85b\u6693\uda56\u895d\u3f2f\udd8e\u8dd1\u33c6\uaf35\uf247\u5b4c\u8255\u5317\u66ea\uefaa\ue518\ud1ef\ucdbe\u521a\u2b99\u13ca\u514e\ue0e7\ubd2f", -4667558, 0x5BC794E1 ^ CryptoVault.mask()));
            return;
        }
        String string = this.makerName.value().trim();
        if (string.isEmpty()) {
            this.backend.setStatus(ConfigsView.nw("\ub0cb\u99a3\uc770\u3117\u4419\udb11\u65cb\u0535\u17a8\ubf77\uc57a\ud212\uf66a\u593c\ubf7d\ud843\u494d\ue6cf\uda9f\u7d0c\u604c\u0477\u520a\u50d9\ue4cb\u081d\u483d\u73fb\u54be\u34d7", -1271976436, 0x5BC794E1 ^ CryptoVault.mask()));
            return;
        }
        String string2 = ConfigMaker.buildConfigJson(this.makerPlan);
        if (string2 == null) {
            this.backend.setStatus(ConfigsView.nw("\u446a\u1604\u9845\u935e\u55ef\u06c4\u8602\uffcc\ua443\u49bf\u531a\ub570\u6010\u948c\u20b6\uf97a\uaf08\ueb75\uadfd\uf044\u3e01\u3613\u86f8\u77cc\uefeb\udb0c\uf83c\u1a44\u214a\u316c\u6a6a\u9756\u5bce\u9bf9\u4c4a\ud831\ub8f6\u5b4d\u0741\u041d\ueaf8\u7b4d\ubb92\ub4a3\ua256\u08f3\u7269\u97cb\u3bbf\uaf17\u2c5a\ud6ed", -1935774492, 0x5BC794E1 ^ CryptoVault.mask()));
            return;
        }
        this.makerSaving = true;
        this.backend.saveGenerated(string, string2, this.makerPlan.gameMode, List.copyOf(this.makerPlan.tags), bl2, bl -> {
            this.makerSaving = false;
            if (bl.booleanValue()) {
                this.makerOpen = false;
            }
        });
    }

    private void drawMaker(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        int n2 = this.makerQuestions.size();
        boolean bl = this.makerStep >= n2;
        float f9 = Math.min(f4, (float)Math.round(720.0f * f6));
        float f10 = f2 + (f4 - f9) * 0.5f;
        builder.text(f10, f3, 13.0f * f6, uiHost.textColor(), ConfigsView.nw("\uf094\uf4ae\uaced\uaf50\ubc24\u522a\u3bd1\u44ea\ud941\u986e\u3e11\ubd0f\u40eb\u74ef\u7793", -1916234547, 0x5BC794E1 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        String string = bl ? ConfigsView.nw("\u7d3c\u6a10\ub7bb\ued83\u0af3\u4e35\ua2da\u9da8\u1257", -1398129784, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\ufc17\u577a\u80eb\u6af4\u42de\uf5e1\u343d\uf5ec\u1367\uff70\uba27", -523721124, 0x5BC794E1 ^ CryptoVault.mask()) + (this.makerStep + 1) + ConfigsView.nw("\ud825\ub55b\ubcc0\u50c2\u68c0", 1287585572, 0x5BC794E1 ^ CryptoVault.mask()) + n2;
        builder.text(f10 + f9 - GpuUiFontMetrics.textWidth(string, 9.5f * f6), f3 + (float)Math.round(2.0f * f6), 9.5f * f6, uiHost.muted2Color(), string);
        float f11 = f3 + (float)Math.round(24.0f * f6);
        float f12 = Math.round(4.0f * f6);
        float f13 = Math.round(6.0f * f6);
        float f14 = (f9 - f13 * (float)(n2 - 1)) / (float)Math.max(1, n2);
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = bl || i2 < this.makerStep ? uiHost.accent() : (i2 == this.makerStep ? UiKit.withAlpha(uiHost.accent(), 112) : uiHost.controlFill(34));
            builder.rect(f10 + (float)i2 * (f14 + f13), f11, f14, f12, f12 * 0.5f, n3);
        }
        float f15 = f11 + f12 + (float)Math.round(18.0f * f6);
        float f16 = Math.round(32.0f * f6);
        float f17 = f3 + f5 - f16;
        float f18 = Math.max(0.0f, f17 - (float)Math.round(12.0f * f6) - f15);
        if (bl) {
            this.drawMakerPreview(builder, uiHost, f10, f15, f9, f18, f6, f7, f8);
        } else {
            this.drawMakerQuestion(builder, uiHost, f10, f15, f9, f6, f7, f8);
        }
        float f19 = Math.round(8.0f * f6);
        if (bl) {
            float f20 = f10;
            float f21 = UiKit.buttonWidth(ConfigsView.nw("\u106a\ua09b\u91a4\u228a\u4680\u6307", -829236566, 0x5BC794E1 ^ CryptoVault.mask()), f6);
            UiKit.button(builder, uiHost, ConfigsView.nw("\uc260\ua918\uf979\ud598\ubf2f\u9613", 995019216, 0x5BC794E1 ^ CryptoVault.mask()), f20, f17, f21, f16, f6, UiKit.Style.SECONDARY, !this.makerSaving, f7, f8, () -> {
                this.makerStep = n2 - 1;
            });
            float f22 = UiKit.buttonWidth(ConfigsView.nw("\u36b7\u6df9\u9089\u9fac\u3cbb\u90b5\uaba6\u778f\u2d35\u1e23\u92d6\ub827\uefe8", 2045707927, 0x5BC794E1 ^ CryptoVault.mask()), f6);
            UiKit.button(builder, uiHost, ConfigsView.nw("\u636e\u3eec\u73ba\u8156\u12b9\u328f\uf0bb\u3cd6\u9883\u1df7\u27a2\u9296", -1149285210, 0x5BC794E1 ^ CryptoVault.mask()), f20 += f21 + f19, f17, f22, f16, f6, UiKit.Style.GHOST, !this.makerSaving, f7, f8, () -> {
                this.makerAnswers.clear();
                this.makerStep = 0;
                this.makerPlan = null;
            });
            float f23 = UiKit.buttonWidth(this.makerSaving ? ConfigsView.nw("\ua29f\u72dd\u8766\ube15\u9728\u29b2\u8718\uabfb\u9729\u916c", 615059107, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\uf35b\u1035\ub999\u30e9\uf6da\u6a6e\u5474\u38e0\u827f\uf195\u71a6\u885b\ue772\u292f\u204c\uaaa0\u7e7d\ubc34\u7897", 1213748428, 0x5BC794E1 ^ CryptoVault.mask()), f6);
            float f24 = UiKit.buttonWidth(ConfigsView.nw("\ub508\udc63\u20b0\ubb1f\ua88b\ue77a\u0224\u11f7\u3b2e\ub746\uea90\uc135\u54d7\u6ef7", 720795519, 0x5BC794E1 ^ CryptoVault.mask()), f6);
            float f25 = f10 + f9 - f23;
            UiKit.button(builder, uiHost, this.makerSaving ? ConfigsView.nw("\u44d0\u71fa\u21b9\u06be\u9f2d\uf592\u5b45\u6956", -1823820945, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\ua2da\ucaf3\uc033\u2da7\ubd2c\ucabe\uaa4f\u9201\u0480\ubd11\ueb31\u8e18\u6d34\u4ac0\ud835\uc837\ud144\ub218\u3369\u8514", 1922093543, 0x5BC794E1 ^ CryptoVault.mask()), f25, f17, f23, f16, f6, UiKit.Style.PRIMARY, !this.makerSaving, f7, f8, () -> this.saveMaker(false));
            UiKit.button(builder, uiHost, ConfigsView.nw("\udfae\ubae3\uf8ec\uf23d\ub5d2\u787f\u92d7\uad8d\u3b09\u3b31\u797c\ua86c\uee50\ud30c", 1719809219, 0x5BC794E1 ^ CryptoVault.mask()), f25 - f19 - f24, f17, f24, f16, f6, UiKit.Style.SECONDARY, !this.makerSaving, f7, f8, () -> this.saveMaker(true));
        } else {
            float f26 = UiKit.buttonWidth(ConfigsView.nw("\ud5f0\u45d9\uc8ea\u8bc0\u33eb\u0c91\ua769\uc46d", 1127141106, 0x5BC794E1 ^ CryptoVault.mask()), f6);
            UiKit.button(builder, uiHost, ConfigsView.nw("\ucb2c\ue629\u43bb\u6b6f\u3d6d\u794d\u1557\u7b10\ua862\u347d", -88255552, 0x5BC794E1 ^ CryptoVault.mask()), f10, f17, f26, f16, f6, UiKit.Style.GHOST, true, f7, f8, () -> {
                this.makerOpen = false;
            });
            boolean bl2 = this.makerAnswers.containsKey(this.makerQuestions.get(this.makerStep).id());
            String string2 = this.makerStep == n2 - 1 ? ConfigsView.nw("\u29c6\ua902\ue9ea\u700c\ua575\u9ff8\ud835\u7c7a\u0106\uef79", 1002479050, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\u852f\uca57\ua59b\u91e1\ue0a6\u49f1\u30de\ub45e", -396781486, 0x5BC794E1 ^ CryptoVault.mask());
            float f27 = Math.max(UiKit.buttonWidth(string2, f6), (float)Math.round(90.0f * f6));
            float f28 = f10 + f9 - f27;
            UiKit.button(builder, uiHost, string2, f28, f17, f27, f16, f6, UiKit.Style.PRIMARY, bl2, f7, f8, this::makerNext);
            float f29 = UiKit.buttonWidth(ConfigsView.nw("\u1fb4\u710e\uee8d\uca98\ua797\u9fdd", -1004413795, 0x5BC794E1 ^ CryptoVault.mask()), f6);
            UiKit.button(builder, uiHost, ConfigsView.nw("\u5d87\u9175\ucab5\ud84c\ub146\uc61a\u3e69", -1464600995, 0x5BC794E1 ^ CryptoVault.mask()), f28 - f19 - f29, f17, f29, f16, f6, UiKit.Style.SECONDARY, this.makerStep > 0, f7, f8, () -> --this.makerStep);
        }
    }

    private void drawMakerQuestion(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7) {
        ConfigMaker.Question question = this.makerQuestions.get(this.makerStep);
        builder.text(f2, f3, 17.0f * f5, uiHost.titleColor(), UiKit.fit(question.title(), f4, 17.0f * f5), UiDrawCommand.TextWeight.SEMIBOLD);
        builder.text(f2, f3 + (float)Math.round(26.0f * f5), 9.5f * f5, uiHost.muted2Color(), UiKit.fit(ConfigsView.nw("\u0ec2\u5983\u9abc\u6779\uc35a\u718b\u2760\uc044\ubd8d\u671c\ua8d3\u47c5\u753a\ua4f9\u08c8\u4f7b\ua475\ue364\u8c55\u57b3\ud956\ub7df\u655c\uf1fa\u5fe4\u0fc3\u1f97\u906d\u6179\uce48\u0dce\ua907\ucae2\u6823\ud168\u448d\u65b0\u3de2\ud737\u609e\ud3eb\u72e4\ue800\u6c7c\u9020\u5c4b\u3070\uf08f\u4433\u3e82\u7636\u2eb0\ucf1e\u313c\u4d7c\ucf79\u3e4e\u3db3\uda64\u2311\u256b\u2fca\ue51f\udcb0\ubb8a", -127138434, 0x5BC794E1 ^ CryptoVault.mask()), f4, 9.5f * f5));
        String string = this.makerAnswers.get(question.id());
        float f8 = f3 + (float)Math.round(48.0f * f5);
        float f9 = Math.round(40.0f * f5);
        float f10 = Math.round(8.0f * f5);
        float f11 = Math.round(12.0f * f5);
        for (ConfigMaker.Option option : question.options()) {
            boolean bl = option.id().equals(string);
            boolean bl2 = uiHost.hovered(f6, f7, f2, f8, f4, f9);
            UiKit.card(builder, uiHost, f2, f8, f4, f9, f5);
            if (bl) {
                builder.rect(f2, f8, f4, f9, f11, UiKit.withAlpha(uiHost.accent(), 24));
                builder.border(f2, f8, f4, f9, f11, 1.4f, UiKit.withAlpha(uiHost.accent(), 176));
            } else if (bl2) {
                builder.rect(f2, f8, f4, f9, f11, uiHost.controlFill(12));
            }
            float f12 = Math.round(12.0f * f5);
            float f13 = f2 + (float)Math.round(16.0f * f5);
            float f14 = f8 + (f9 - f12) * 0.5f;
            builder.rect(f13, f14, f12, f12, f12 * 0.5f, bl ? uiHost.accent() : uiHost.controlFill(38));
            if (!bl) {
                builder.border(f13, f14, f12, f12, f12 * 0.5f, 1.0f, 0x22FFFFFF);
            }
            float f15 = f13 + f12 + (float)Math.round(12.0f * f5);
            builder.text(f15, GpuUiFontMetrics.topInBox(f8, f9, 11.5f * f5), 11.5f * f5, bl ? uiHost.textColor() : uiHost.mutedColor(), UiKit.fit(option.label(), f2 + f4 - f15 - (float)Math.round(12.0f * f5), 11.5f * f5), UiDrawCommand.TextWeight.SEMIBOLD);
            String string2 = question.id();
            String string3 = option.id();
            uiHost.hit(f2, f8, f4, f9, () -> this.makerAnswers.put(string2, string3));
            f8 += f9 + f10;
        }
    }

    private void drawMakerPreview(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        UiKit.label(builder, uiHost, ConfigsView.nw("\uc8f4\u2673\uc95c\u9390\uf2e9\u3372", 266524202, 0x5BC794E1 ^ CryptoVault.mask()), f2, f3, f6);
        float f9 = f3 + (float)Math.round(16.0f * f6);
        float f10 = Math.round(34.0f * f6);
        this.makerName.draw(builder, uiHost, f2, f9, f4, f10, f6);
        float f11 = f9 + f10 + (float)Math.round(10.0f * f6);
        float f12 = f2;
        if (this.makerPlan != null) {
            f12 += UiKit.chip(builder, uiHost, this.makerPlan.gameMode, f12, f11, f6, uiHost.accent()) + (float)Math.round(6.0f * f6);
            for (String object2 : this.makerPlan.tags) {
                f12 += UiKit.chip(builder, uiHost, object2, f12, f11, f6, uiHost.mutedColor()) + (float)Math.round(5.0f * f6);
            }
        }
        int n2 = 0;
        for (ConfigMaker.PreviewModule previewModule : this.makerPreview) {
            if (!previewModule.enabled()) continue;
            ++n2;
        }
        int n3 = this.makerPreview.size() - n2;
        String string = n2 + ConfigsView.nw("\ufdd3\u7dba\u1574\ud243\uc55d\ucb25\u790b\u2686\u5c98B", -1619359719, 0x5BC794E1 ^ CryptoVault.mask()) + (n2 == 1 ? "" : "s") + ConfigsView.nw("\uee31\u847d\ufbaa\u2b05\u2bc8", -209582977, 0x5BC794E1 ^ CryptoVault.mask()) + (n3 > 0 ? ", " + n3 + ConfigsView.nw("\uce43\udbd8\u23e8\u83e0\u0260\u3434\uda57\u1f6b\ub433\u8dd5\u2d5b\udcb4\u0592\u276e\u903a\uf967\u5872\u5f4a\u4627", 43832878, 0x5BC794E1 ^ CryptoVault.mask()) : "");
        float f13 = GpuUiFontMetrics.textWidth(string, 9.5f * f6);
        if (f2 + f4 - f13 > f12 + (float)Math.round(8.0f * f6)) {
            builder.text(f2 + f4 - f13, f11 + (float)Math.round(3.0f * f6), 9.5f * f6, uiHost.muted2Color(), string);
        }
        float f14 = f11 + (float)Math.round(28.0f * f6);
        if (this.makerPlan != null) {
            for (String string2 : this.makerPlan.warnings) {
                builder.text(f2, f14, 9.5f * f6, -14741, UiKit.fit(string2, f4, 9.5f * f6));
                f14 += (float)Math.round(16.0f * f6);
            }
            if (!this.makerPlan.warnings.isEmpty()) {
                f14 += (float)Math.round(4.0f * f6);
            }
        }
        float f15 = Math.max(0.0f, f3 + f5 - f14);
        this.makerRect[0] = f2;
        this.makerRect[1] = f14;
        this.makerRect[2] = f4;
        this.makerRect[3] = f15;
        if (this.makerPreview.isEmpty()) {
            UiKit.emptyState(builder, uiHost, ConfigsView.nw("\u3845\u8b90\u9b20\u893e\u3ef7\ue9fc\u03d8\ubb80\ufaff\u4a6b\u39c9\ud7ac\u556b\ua50e\u57fe\uc228\u4380\u37fc\u2a59\u7fae\u4514\u7e0e", -982553691, 0x5BC794E1 ^ CryptoVault.mask()), ConfigsView.nw("\uc70f\u8467\ua6b6\u569e\u77d2\u1ace\ua395\u312e\u5ff3\uf0d1\u6131\ufbae\u4807\u4811\u398d\u2f21\uc585\uec55\ufae8\ub2b9\u5a4f\u103b\u60b9\u4544\ubdb9\ud041\u45f3\u7365\u6afa\u34a7\ufbc5\u6f6b\u21e8\u1b8d\u314e\uf2f9\u0cee\u423a\u08d5\u5c16\uebcb\ub25b\u9c47\ucf44\ubcea\u3a11\u1de7\uec58\uced5\u9d58\u9719\u2cd2\u9ff9\u4bfe\u4fa6\ue23f\u55eb\u9abf\u565a\u82b2\u9097\u8fb1", -467214743, 0x5BC794E1 ^ CryptoVault.mask()), f2, f14 + (float)Math.round(30.0f * f6), f4, f6);
            return;
        }
        float f16 = Math.round(22.0f * f6);
        float f17 = Math.round(46.0f * f6);
        float f18 = Math.round(6.0f * f6);
        ArrayList<float[]> arrayList = new ArrayList<float[]>();
        ArrayList<String> arrayList2 = new ArrayList<String>();
        String string3 = null;
        float f19 = 0.0f;
        for (int i2 = 0; i2 < this.makerPreview.size(); ++i2) {
            Object object;
            ConfigMaker.PreviewModule previewModule = this.makerPreview.get(i2);
            Object object2 = object = previewModule.enabled() ? previewModule.category().toUpperCase(Locale.ROOT) : ConfigsView.nw("\u0fbb\u44ee\u7959\u79f1\ubcde\ubc00\ue67e\uc78e\u0f38\ue56f\uf063\uad03\u7a9f\u8eff\uce0c\ue2a2\u9998\uebe6\u0664\u93ff\u4947\uf5b2\u7cf0\u6aac\u0dfa\u023b\ua0ca\u284d\ued33\uf758\u67aa\u4448\u070f\u91fb\uef0c\u6c8d\ud35d\u0b52\u5c0e\u55a8\u3290", -94451189, 0x5BC794E1 ^ CryptoVault.mask());
            if (!((String)object).equals(string3)) {
                string3 = object;
                arrayList2.add((String)object);
                arrayList.add(new float[]{f19, -arrayList2.size()});
                f19 += f16;
            }
            arrayList.add(new float[]{f19, i2});
            f19 += f17 + f18;
        }
        this.makerScroll.update(uiHost.frameDelta(), f15, Math.max(0.0f, f19 - f18), uiHost.now());
        float f20 = this.makerScroll.offset();
        builder.clipPush(f2, f14, f4, f15, 0.0f);
        uiHost.beginHitClip(f2, f14, f4, f15);
        float f21 = Math.round(14.0f * f6);
        for (float[] fArray : arrayList) {
            float f22 = f14 + fArray[0] - f20;
            int n4 = (int)fArray[1];
            if (n4 < 0) {
                if (f22 + f16 < f14 || f22 > f14 + f15) continue;
                builder.text(f2 + (float)Math.round(2.0f * f6), f22 + (float)Math.round(6.0f * f6), 9.0f * f6, uiHost.muted2Color(), (String)arrayList2.get(-n4 - 1), UiDrawCommand.TextWeight.SEMIBOLD);
                continue;
            }
            if (f22 + f17 < f14 || f22 > f14 + f15) continue;
            ConfigMaker.PreviewModule previewModule = this.makerPreview.get(n4);
            UiKit.card(builder, uiHost, f2, f22, f4, f17, f6);
            String string4 = previewModule.enabled() ? "On" : ConfigsView.nw("\u112d\u2171\u2a32\u2d66", -634610101, 0x5BC794E1 ^ CryptoVault.mask());
            float f23 = Math.round(GpuUiFontMetrics.textWidth(string4, 8.5f * f6, UiDrawCommand.TextWeight.SEMIBOLD) + 16.0f * f6);
            UiKit.chip(builder, uiHost, string4, f2 + f4 - f21 - f23, f22 + (float)Math.round(9.0f * f6), f6, previewModule.enabled() ? -9247307 : uiHost.mutedColor());
            builder.text(f2 + f21, f22 + (float)Math.round(9.0f * f6), 11.5f * f6, uiHost.textColor(), UiKit.fit(previewModule.name(), f4 - f21 * 2.0f - f23 - (float)Math.round(8.0f * f6), 11.5f * f6), UiDrawCommand.TextWeight.SEMIBOLD);
            String string5 = previewModule.highlights().isEmpty() ? ConfigsView.nw("\u2603\ud6e5\u88b7\u72d3\ufae3\u0e33\u93c9\ue8e5\ua479\u735e\u33a5\u1933\ua7b7\ud0f1\u78ed\u7500\uddc0\u983c", 2067147788, 0x5BC794E1 ^ CryptoVault.mask()) : String.join((CharSequence)ConfigsView.nw("\u5712\uc4c8\ua75e\u3eb6\ueeda\u8f1b\u85af\u0092", -424751477, 0x5BC794E1 ^ CryptoVault.mask()), previewModule.highlights());
            builder.text(f2 + f21, f22 + (float)Math.round(27.0f * f6), 9.5f * f6, uiHost.mutedColor(), UiKit.fit(string5, f4 - f21 * 2.0f, 9.5f * f6));
        }
        uiHost.endHitClip();
        builder.clipPop();
        this.makerScroll.paintBar(builder, f2 + f4 + (float)Math.round(4.0f * f6), f14, f15, f6, uiHost.textColor(), uiHost.now());
    }

    private float drawTagChip(UiDisplayList.Builder builder, UiHost uiHost, String string, boolean bl, boolean bl2, float f2, float f3, float f4, float f5, float f6, Runnable runnable) {
        boolean bl3;
        float f7 = 9.0f * f4;
        float f8 = Math.round(GpuUiFontMetrics.textWidth(string, f7, UiDrawCommand.TextWeight.SEMIBOLD) + 18.0f * f4);
        float f9 = Math.round(20.0f * f4);
        boolean bl4 = bl3 = bl2 && uiHost.hovered(f5, f6, f2, f3, f8, f9);
        builder.rect(f2, f3, f8, f9, f9 * 0.5f, bl ? UiKit.withAlpha(uiHost.accent(), 48) : uiHost.controlFill(bl3 ? 34 : 18));
        builder.border(f2, f3, f8, f9, f9 * 0.5f, 1.0f, bl ? UiKit.withAlpha(uiHost.accent(), 144) : uiHost.lineColor());
        builder.text(f2 + 9.0f * f4, GpuUiFontMetrics.topInBox(f3, f9, f7), f7, bl ? uiHost.accent() : uiHost.muted2Color(), string, UiDrawCommand.TextWeight.SEMIBOLD);
        if (bl2 && runnable != null) {
            uiHost.hit(f2, f3, f8, f9, runnable);
        }
        return f8;
    }

    private float drawTagToggles(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, Set<String> set, Consumer<String> consumer) {
        float f8 = Math.round(6.0f * f5);
        float f9 = Math.round(20.0f * f5);
        float f10 = Math.round(6.0f * f5);
        float f11 = f2;
        float f12 = f3;
        for (String string : ConfigTags.ALL) {
            float f13 = Math.round(GpuUiFontMetrics.textWidth(string, 9.0f * f5, UiDrawCommand.TextWeight.SEMIBOLD) + 18.0f * f5);
            if (f11 + f13 > f2 + f4) {
                f11 = f2;
                f12 += f9 + f10;
            }
            this.drawTagChip(builder, uiHost, string, set.contains(string), true, f11, f12, f5, f6, f7, () -> consumer.accept(string));
            f11 += f13 + f8;
        }
        return f12 - f3 + f9;
    }

    private void drawMine(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9;
        float f10;
        String string2;
        float f11;
        float f12 = Math.round(20.0f * f6);
        float f13 = Math.round(Math.min(430.0f * f6, f4 * 0.52f));
        float f14 = f2 + f13 + f12;
        float f15 = f4 - f13 - f12;
        String string3 = this.backend.loadedConfig();
        boolean bl = string3 != null && this.backend.unsavedChanges();
        List<String> list = this.backend.savedConfigs();
        UiKit.label(builder, uiHost, ConfigsView.nw("\ua3fe\u87ac\u9d8b\u8534\u5ba9\ub118\u2406\ub2c0\u8cca\u6ed9\u16d2\u3ea9\uc549\u28d3", -1942293501, 0x5BC794E1 ^ CryptoVault.mask()), f2, f3, f6);
        String string4 = this.backend.cloudCount() + ConfigsView.nw("\u4290\u0e11\u673d\uf5ae\ub917\uc6f0", 1342663457, 0x5BC794E1 ^ CryptoVault.mask()) + this.backend.cloudLimit() + ConfigsView.nw("\u1ac3\u19e3\uf46e\uce36\u11a7\u4473\ub851\uefcd\u2131\u84b7\u076b\uc477\u72bb", -1864448461, 0x5BC794E1 ^ CryptoVault.mask());
        float f16 = 9.5f * f6;
        builder.text(f2 + f13 - GpuUiFontMetrics.textWidth(string4, f16), f3, f16, uiHost.muted2Color(), string4);
        float f17 = f3 + (float)Math.round(20.0f * f6);
        float f18 = Math.max(0.0f, f5 - (float)Math.round(20.0f * f6));
        this.mineRect[0] = f2;
        this.mineRect[1] = f17;
        this.mineRect[2] = f13;
        this.mineRect[3] = f18;
        float f19 = Math.round(58.0f * f6);
        float f20 = Math.round(8.0f * f6);
        float f21 = f19 + f20;
        this.mineScroll.update(uiHost.frameDelta(), f18, Math.max(0.0f, (float)list.size() * f21 - f20), uiHost.now());
        float f22 = this.mineScroll.offset();
        if (list.isEmpty()) {
            UiKit.emptyState(builder, uiHost, ConfigsView.nw("\u69e1\ufd3a\u870e\uf001\u876a\u86a2\u64a3\uf68e\u7019\ua40e\u65dd\u00d5\u2ee2\uc814\u97b9", 49132531, 0x5BC794E1 ^ CryptoVault.mask()), ConfigsView.nw("\uaf9f\u3960\ud262\u9d7a\u6474\u3fa8\u1d06\ud9cb\u3a11\ud476\u0a11\u3341\u8f8b\u1f69\u9865\u211b\u4b1a\u8589\u363b\uf156\uf7ad\ub92e\u6cab\ua7ba\u121a\u73b6\u247a\ua331\u090a\u02a0\u5a63\u27dd\ub352\ua6ae\uec86\ucb0e\ub7cb\ub23d\u80d7\u5ecc\u4155\u9cee\u83ad\ubb35", -2126200483, 0x5BC794E1 ^ CryptoVault.mask()), f2, f17 + (float)Math.round(36.0f * f6), f13, f6);
        }
        builder.clipPush(f2, f17, f13, f18, 0.0f);
        uiHost.beginHitClip(f2, f17, f13, f18);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            float f23;
            f11 = f17 + (float)i2 * f21 - f22;
            if (f11 + f19 < f17 || f11 > f17 + f18) continue;
            string2 = list.get(i2);
            Info info = this.backend.info(string2);
            boolean bl2 = info != null && info.active();
            boolean bl3 = string2.equals(this.editName);
            boolean bl4 = uiHost.hovered(f7, f8, f2, f11, f13, f19);
            UiKit.card(builder, uiHost, f2, f11, f13, f19, f6);
            if (bl4 && !bl3) {
                builder.rect(f2, f11, f13, f19, Math.round(12.0f * f6), uiHost.controlFill(12));
            }
            if (bl3) {
                builder.border(f2, f11, f13, f19, Math.round(12.0f * f6), 1.4f, UiKit.withAlpha(uiHost.accent(), 192));
            } else if (bl2) {
                builder.border(f2, f11, f13, f19, Math.round(12.0f * f6), 1.0f, UiKit.withAlpha(uiHost.accent(), 144));
                builder.rect(f2 + (float)Math.round(7.0f * f6), f11 + (float)Math.round(14.0f * f6), Math.round(3.0f * f6), f19 - (float)Math.round(28.0f * f6), Math.round(1.5f * f6), uiHost.accent());
            }
            f10 = f2 + (float)Math.round(20.0f * f6);
            float f24 = Math.round(62.0f * f6);
            f9 = Math.round(28.0f * f6);
            float f25 = f11 + (f19 - f9) * 0.5f;
            float f26 = f2 + f13 - f24 - (float)Math.round(14.0f * f6);
            float f27 = f26 - f10 - (float)Math.round(10.0f * f6);
            builder.text(f10, f11 + (float)Math.round(12.0f * f6), 12.5f * f6, uiHost.textColor(), UiKit.fit(string2, f27, 12.5f * f6), UiDrawCommand.TextWeight.SEMIBOLD);
            if (info == null) {
                f23 = f10;
                f23 += UiKit.chip(builder, uiHost, ConfigsView.nw("\u2567\ubfa5\ub580\uc035\u43a5\u86ad\ufd28\ucccf", -1665974135, 0x5BC794E1 ^ CryptoVault.mask()), f23, f11 + (float)Math.round(31.0f * f6), f6, uiHost.mutedColor()) + (float)Math.round(6.0f * f6);
                if (bl && string2.equalsIgnoreCase(string3)) {
                    UiKit.chip(builder, uiHost, ConfigsView.nw("\u549c\u8064\u7dfc\u5500\ud469\uf85b\u6b2f\uae8f", -129912301, 0x5BC794E1 ^ CryptoVault.mask()), f23, f11 + (float)Math.round(31.0f * f6), f6, -14741);
                }
            } else {
                f23 = f10;
                f23 += UiKit.chip(builder, uiHost, ConfigsView.nw("\ued11\u6f3f\ub638\u4854\u6b1c\ua9a8\u88fd", -404890018, 0x5BC794E1 ^ CryptoVault.mask()), f23, f11 + (float)Math.round(31.0f * f6), f6, -9651969) + (float)Math.round(6.0f * f6);
                f23 += UiKit.chip(builder, uiHost, info.isPublic() ? ConfigsView.nw("\u1f5b\u96c2\uc6dc\u47ee\ube06\u7d64\u3a17\u3f93\u2ea8\u769f", -774181728, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\uc06c\u1379\u14bf\u7cfc\u84c8\uca2e\u71f7\uefc9\u9a7e", -932428679, 0x5BC794E1 ^ CryptoVault.mask()), f23, f11 + (float)Math.round(31.0f * f6), f6, info.isPublic() ? -9247307 : -14741) + (float)Math.round(6.0f * f6);
                if (bl2) {
                    f23 += UiKit.chip(builder, uiHost, ConfigsView.nw("\ue82a\u4e18\u9f9b\uca3b\uf692\ubc04\u67ee", 991544894, 0x5BC794E1 ^ CryptoVault.mask()), f23, f11 + (float)Math.round(31.0f * f6), f6, uiHost.accent()) + (float)Math.round(6.0f * f6);
                }
                if (bl && string2.equalsIgnoreCase(string3)) {
                    f23 += UiKit.chip(builder, uiHost, ConfigsView.nw("\u9e6f\u80f2\u6ebb\ubb9b\u421b\u914c\u1b1d\u6c2e\u01c0\u6c0e", -928802503, 0x5BC794E1 ^ CryptoVault.mask()), f23, f11 + (float)Math.round(31.0f * f6), f6, -14741) + (float)Math.round(6.0f * f6);
                }
                List<String> list2 = info.tags();
                for (int i3 = 0; i3 < list2.size() && f23 < f26 - 50.0f * f6; f23 += UiKit.chip(builder, uiHost, list2.get(i3), f23, f11 + (float)Math.round(31.0f * f6), f6, uiHost.mutedColor()) + (float)Math.round(5.0f * f6), ++i3) {
                }
            }
            UiKit.button(builder, uiHost, ConfigsView.nw("\ua2ce\u4412\u51bf\uc2ab\u20e2\uc62b\u9b9b\u3bf0", 1026678861, 0x5BC794E1 ^ CryptoVault.mask()), f26, f25, f24, f9, f6, UiKit.Style.PRIMARY, true, f7, f8, () -> this.backend.load(string2));
            f23 = f26 - f2 - (float)Math.round(6.0f * f6);
            uiHost.hit(f2, f11, f23, f19, () -> this.backend.load(string2));
            uiHost.hitSecondary(f2, f11, f13, f19, () -> this.openRowMenu(string2, info));
        }
        uiHost.endHitClip();
        builder.clipPop();
        this.mineScroll.paintBar(builder, f2 + f13 + (float)Math.round(4.0f * f6), f17, f18, f6, uiHost.textColor(), uiHost.now());
        float f28 = Math.round(18.0f * f6);
        f11 = Math.round(210.0f * f6);
        UiKit.card(builder, uiHost, f14, f3, f15, f11, f6);
        string2 = string3 != null ? ConfigsView.nw("\u2290\u9a3e\uc86b\uf1f2\u6756\u8e12\u481f\u6539\u0c8b\uc3da\uce9d\u2e98\udbc7\u7aa7\uf3f9\udfd6\u0c53\u402d\uac25\u5c4e", -1018479619, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\u8d0a\uab48\uaf38\ubf3b\ud7be\u8ccf\uf00a\uf951\u9200\u7a1b\u4eff\ufda2\u8525\ucbb1\u09b7\u9f11\u98c2\u21aa\u8f5b\u3bcb\u3cd3\u7d27\u63cd", 1956177004, 0x5BC794E1 ^ CryptoVault.mask());
        builder.text(f14 + f28, f3 + (float)Math.round(16.0f * f6), 13.0f * f6, uiHost.textColor(), string2, UiDrawCommand.TextWeight.SEMIBOLD);
        if (string3 != null) {
            float f29 = GpuUiFontMetrics.textWidth(string2, 13.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD);
            float f30 = f14 + f15 - f28;
            float f31 = Math.round(26.0f * f6);
            float f32 = f3 + (float)Math.round(10.0f * f6);
            if (bl) {
                f10 = UiKit.buttonWidth(ConfigsView.nw("\u905b\u3680\u486e\u5ade\uf385\ua842\u89de\uc8f9\u6aea\u3c9c", 1660153743, 0x5BC794E1 ^ CryptoVault.mask()), f6);
                UiKit.button(builder, uiHost, ConfigsView.nw("\uf682\u2395\u1c5a\ua4bc\ueef4\ubc9b\uaf9d\uc97e\ufada\uc8a9", 572601714, 0x5BC794E1 ^ CryptoVault.mask()), f30 - f10, f32, f10, f31, f6, UiKit.Style.PRIMARY, this.backend.signedIn(), f7, f8, this.backend::overwrite);
                String string5 = UiKit.fit(ConfigsView.nw("\ubb43\u8539\ue0a4\uc3b8\ua904\u17d6\u3165\uced8\u064b\u8033\u2ff2\u3def\u595c\ucc88\u6f4e\u0bde\u992a\ub84a\u54e7\u009c\uea02", -1182788152, 0x5BC794E1 ^ CryptoVault.mask()) + string3 + "\"", Math.max(0.0f, f30 - f10 - (float)Math.round(10.0f * f6) - (f14 + f28 + f29 + (float)Math.round(12.0f * f6))), 9.5f * f6);
                builder.text(f30 - f10 - (float)Math.round(10.0f * f6) - GpuUiFontMetrics.textWidth(string5, 9.5f * f6), GpuUiFontMetrics.topInBox(f32, f31, 9.5f * f6), 9.5f * f6, -14741, string5);
            } else {
                String string6 = UiKit.fit("\"" + string3 + ConfigsView.nw("\u34b8\u843e\u355a\u8f3b\ud38f\u3b91\ud80f\u69d6\ue121\u8c39\ud33b\uba25\u13e7\u614c\ubb9b\u3f08\uc46c\u3a13", 1018355901, 0x5BC794E1 ^ CryptoVault.mask()), Math.max(0.0f, f30 - (f14 + f28 + f29 + (float)Math.round(12.0f * f6))), 9.5f * f6);
                builder.text(f30 - GpuUiFontMetrics.textWidth(string6, 9.5f * f6), GpuUiFontMetrics.topInBox(f32, f31, 9.5f * f6), 9.5f * f6, uiHost.muted2Color(), string6);
            }
        }
        float f33 = UiKit.buttonWidth(ConfigsView.nw("\ue769\u2fbb\uf0f3\u7be9\u46cd\u6df7\uad8a\u3a12", -2048867177, 0x5BC794E1 ^ CryptoVault.mask()), f6) + (float)Math.round(10.0f * f6);
        float f34 = Math.round(34.0f * f6);
        float f35 = f3 + (float)Math.round(44.0f * f6);
        this.saveName.draw(builder, uiHost, f14 + f28, f35, f15 - f28 * 2.0f - f33 - (float)Math.round(10.0f * f6), f34, f6);
        UiKit.button(builder, uiHost, ConfigsView.nw("\uecfe\uc1a5\uc633\u4f9a\u116d\uc187", 377712138, 0x5BC794E1 ^ CryptoVault.mask()), f14 + f15 - f28 - f33, f35, f33, f34, f6, UiKit.Style.PRIMARY, true, f7, f8, this::saveCurrent);
        float f36 = f35 + f34 + (float)Math.round(12.0f * f6);
        UiKit.toggle(builder, uiHost, f14 + f28, f36, f6, this.newPublic, () -> {
            this.newPublic = !this.newPublic;
        });
        builder.text(f14 + f28 + (float)Math.round(42.0f * f6), GpuUiFontMetrics.topInBox(f36, Math.round(18.0f * f6), 10.5f * f6), 10.5f * f6, uiHost.textColor(), this.newPublic ? ConfigsView.nw("\u233b\u3ee6\u867b\u1371\u86df\uba07\u7cac\u2a5a\uebb6\u67bb\ude3d\ue8df\u5719\uaa61\uc7da\u0237\uee21\u47cb\u3979\u4d71\uffc9\u1422\ufeb7\u3269\ud46a\ua3bb\uff88\u88a6\u2bb7\u1f17\uf0ed\u0204\u9e73\u76f7\ubdb5", -1324686222, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\u2c83\u9a2a\ua1d8\ueb56\uc4bb\u265b\u8f1b\ube2a\udc35\u85f4\uf9de\ue3b9\uccd4\u773c\u6c82\u5b2c\u0e6e\ud213\uf8fd\u0b31\u8e08", -1006892007, 0x5BC794E1 ^ CryptoVault.mask()));
        float f37 = f36 + (float)Math.round(28.0f * f6);
        builder.text(f14 + f28, f37, 9.0f * f6, uiHost.muted2Color(), ConfigsView.nw("\u7970\u8501\u98fa\ubfb1\ud7a8\u2f2b\u2624\u10bd", -208474040, 0x5BC794E1 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        this.drawTagToggles(builder, uiHost, f14 + f28, f37 + (float)Math.round(14.0f * f6), f15 - f28 * 2.0f, f6, f7, f8, this.newTags, string -> {
            if (!this.newTags.remove(string)) {
                this.newTags.add((String)string);
            }
        });
        float f38 = f3 + f11 + (float)Math.round(16.0f * f6);
        f9 = Math.max((float)Math.round(120.0f * f6), f5 - f11 - (float)Math.round(16.0f * f6));
        this.drawHubPointer(builder, uiHost, f14, f38, f15, f6, f7, f8);
    }

    private void openRowMenu(String string, Info info) {
        this.menuName = string;
        this.menuInfo = info;
        this.menuX = this.lastMx;
        this.menuY = this.lastMy;
    }

    private boolean deletable(String string, Info info) {
        return info != null ? this.backend.signedIn() : this.backend.canDeleteLocal(string);
    }

    private void deleteConfig(String string, Info info) {
        if (info != null) {
            this.backend.deleteCloud(string);
        } else if (this.backend.canDeleteLocal(string)) {
            this.backend.deleteLocal(string);
        } else {
            return;
        }
        ClientPrefs.get().renameConfig(string, null);
        ConfigManager.getInstance().onConfigRenamed(string, null);
        if (string.equals(this.editName)) {
            this.editName = null;
            this.armedDeleteName = null;
        }
    }

    private void drawOverlays(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        if (this.confirmDeleteName != null) {
            this.menuName = null;
            builder.rect(f2, f3, f4, f5, 0.0f, -1728053248);
            uiHost.hit(f2, f3, f4, f5, () -> {});
            uiHost.hitSecondary(f2, f3, f4, f5, () -> {});
            float f9 = Math.min(f4 - (float)Math.round(40.0f * f6), (float)Math.round(340.0f * f6));
            float f10 = Math.round(132.0f * f6);
            float f11 = f2 + (f4 - f9) * 0.5f;
            float f12 = f3 + (f5 - f10) * 0.5f;
            float f13 = Math.round(18.0f * f6);
            builder.shadow(f11, f12, f9, f10, Math.round(12.0f * f6), Math.round(24.0f * f6), 0.0f, Integer.MIN_VALUE);
            uiHost.panel(builder, f11, f12, f9, f10, Math.round(12.0f * f6 * uiHost.cornerScale()));
            UiKit.card(builder, uiHost, f11, f12, f9, f10, f6);
            builder.text(f11 + f13, f12 + (float)Math.round(18.0f * f6), 13.0f * f6, uiHost.textColor(), UiKit.fit(ConfigsView.nw("\u0ff3\uf746\uc6b7\ue38d\u2f62\ucf68\udc77\u3585\u7505\u0786", -2125857448, 0x5BC794E1 ^ CryptoVault.mask()) + this.confirmDeleteName + "\"?", f9 - f13 * 2.0f, 13.0f * f6, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
            String string = this.confirmDeleteInfo != null ? ConfigsView.nw("\u6d21\ub647\u9fbc\u0a51\uf714\u34f3\ud969\u0a7e\u95b6\ud836\uc9f4\u53e9\u884b\uc894\u213d\u4003\uc860\ua74e\u4ed8\uc51a\u48ed\u56d7\u6d46\uf7a9\ub05e\u67e3\u40af\u0f90\u33c6\u66f7\uc9dd\uac49\u1234\u46b8\u6c44\uad29\udeca\u1dcb\u3177\u7d26\uc6da\u041d\u4efe\udff7\ue56e\ua4a3\u14a5\ud0b8\u983d\u97e4\u76e8\ud879\u5dbe\u9636\ub032\ua732\u2071\u1497\uf578\u1322\ub448\u1c2c\u1b54", 1696689002, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\ud86e\u7b74\u0cd0I\uafeb\u4985\uf256\ucf1d\ua6cd\u4684\u2570\u49c5\u8090\u4104\u4dec\u4a33\u3f16\u34dd\ud9c2\u8cae\ue177\u3ce6\u075e\u9c14\u42e3\u0c33\u0dfe\uce1b\u05bf\uc302\u68ec\u495a\u196d\u042d\u77ee\u7168\u5064\u7aa7\u7153\ud76b\uef43\u8877\uf5b9\ua244\uc4ce\u4883\uf741\ub91b\u5b62\u5ef8\u0d36\ub075\u32bc\u95d9\u8af3", 538378078, 0x5BC794E1 ^ CryptoVault.mask());
            List<String> list = UiKit.wrap(string, f9 - f13 * 2.0f, 10.0f * f6, 2);
            for (int i2 = 0; i2 < list.size(); ++i2) {
                builder.text(f11 + f13, f12 + (float)Math.round(42.0f * f6) + (float)(i2 * Math.round(14.0f * f6)), 10.0f * f6, uiHost.mutedColor(), list.get(i2));
            }
            float f14 = Math.round(30.0f * f6);
            float f15 = UiKit.buttonWidth(ConfigsView.nw("\u81e8\ub320\u707c\u139c\u6b61\u5346\u471b", -1135687500, 0x5BC794E1 ^ CryptoVault.mask()), f6);
            float f16 = UiKit.buttonWidth(ConfigsView.nw("\u9a41\u348e\u1cb3\uaafe\u4b30\u3d54\ud211\uff5e\u511a", 624866459, 0x5BC794E1 ^ CryptoVault.mask()), f6);
            float f17 = f12 + f10 - f13 - f14;
            String string2 = this.confirmDeleteName;
            Info info = this.confirmDeleteInfo;
            UiKit.button(builder, uiHost, ConfigsView.nw("\uf023\u45f6\ub8e5\u0318\u29a3\u9ff8\u6490\ud759", -1314476375, 0x5BC794E1 ^ CryptoVault.mask()), f11 + f9 - f13 - f15, f17, f15, f14, f6, UiKit.Style.DANGER, true, f7, f8, () -> {
                this.deleteConfig(string2, info);
                this.confirmDeleteName = null;
                this.confirmDeleteInfo = null;
            });
            UiKit.button(builder, uiHost, ConfigsView.nw("\u7b20\u6d5d\udeb0\u8a6b\u32af\u804b\uc035", 1521345683, 0x5BC794E1 ^ CryptoVault.mask()), f11 + f9 - f13 - f15 - (float)Math.round(8.0f * f6) - f16, f17, f16, f14, f6, UiKit.Style.SECONDARY, true, f7, f8, () -> {
                this.confirmDeleteName = null;
                this.confirmDeleteInfo = null;
            });
            return;
        }
        if (this.menuName == null) {
            return;
        }
        uiHost.hit(f2, f3, f4, f5, () -> {
            this.menuName = null;
        });
        uiHost.hitSecondary(f2, f3, f4, f5, () -> {
            this.menuName = null;
        });
        String string = this.menuName;
        Info info = this.menuInfo;
        String[] stringArray = new String[]{ConfigsView.nw("\u2b38\u799d\ucc57\u19d5\u78e7", 11414597, 0x5BC794E1 ^ CryptoVault.mask()), ConfigsView.nw("\u65e0\u8678\u36c0\uee29\ueb0f\ua462\u919f\u61f1\ubaa7\u7845\uc199", -2109896266, 0x5BC794E1 ^ CryptoVault.mask()), ConfigsView.nw("\ubc47\u9617\u3c6f\ub58b\u3ff7\u374a\ub1a2\uad10", 1287174185, 0x5BC794E1 ^ CryptoVault.mask())};
        boolean[] blArray = new boolean[]{true, true, this.deletable(string, info)};
        float f18 = Math.round(28.0f * f6);
        float f19 = Math.round(5.0f * f6);
        float f20 = Math.round(150.0f * f6);
        float f21 = f18 * (float)stringArray.length + f19 * 2.0f;
        float f22 = Math.min(this.menuX, f2 + f4 - f20 - f19);
        float f23 = Math.min(this.menuY, f3 + f5 - f21 - f19);
        float f24 = Math.round(8.0f * f6 * Math.min(1.0f, uiHost.cornerScale()));
        builder.shadow(f22, f23, f20, f21, f24, Math.round(16.0f * f6), 0.0f, 0x70000000);
        builder.rect(f22, f23, f20, f21, f24, UiKit.panelOpaque());
        builder.rect(f22, f23, f20, f21, f24, uiHost.controlFill(16));
        builder.border(f22, f23, f20, f21, f24, 1.0f, uiHost.lineColor());
        for (int i3 = 0; i3 < stringArray.length; ++i3) {
            boolean bl;
            float f25 = f23 + f19 + (float)i3 * f18;
            boolean bl2 = bl = blArray[i3] && uiHost.hovered(f7, f8, f22 + f19, f25, f20 - f19 * 2.0f, f18);
            if (bl) {
                builder.rect(f22 + f19, f25, f20 - f19 * 2.0f, f18, Math.max(0.0f, f24 - 3.0f * f6), uiHost.controlFill(36));
            }
            int n2 = !blArray[i3] ? uiHost.muted2Color() : (i3 == 2 ? -38037 : uiHost.textColor());
            float f26 = 10.5f * f6;
            builder.text(f22 + f19 + (float)Math.round(10.0f * f6), GpuUiFontMetrics.topInBox(f25, f18, f26), f26, n2, stringArray[i3]);
            if (!blArray[i3]) continue;
            int n3 = i3;
            uiHost.hit(f22 + f19, f25, f20 - f19 * 2.0f, f18, () -> {
                this.menuName = null;
                if (n3 == 0) {
                    this.backend.load(string);
                } else if (n3 == 1) {
                    this.openEdit(string, info);
                } else {
                    this.confirmDeleteName = string;
                    this.confirmDeleteInfo = info;
                }
            });
        }
    }

    private void openEdit(String string, Info info) {
        this.editName = string;
        this.armedDeleteName = null;
        this.renameField.setValue("");
        this.editTags.clear();
        if (info != null) {
            this.editTags.addAll(info.tags());
        }
        this.editTagsFor = string;
    }

    public boolean editing() {
        return this.editName != null && this.confirmDeleteName == null && this.menuName == null;
    }

    public void closeEdit() {
        this.editName = null;
        this.armedDeleteName = null;
    }

    public void drawEditFloating(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        if (this.editName != null) {
            this.drawEditPanel(builder, uiHost, f2, f3, f4, f5, f6, f7, f8);
        }
    }

    private void drawEditPanel(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        Info info = this.backend.info(this.editName);
        float f9 = Math.round(18.0f * f6);
        builder.text(f2 + f9, f3 + (float)Math.round(16.0f * f6), 13.0f * f6, uiHost.textColor(), UiKit.fit(ConfigsView.nw("\ucbe2\u2c53\uc3cd\ud32e\ue764\ud9f1\ua085", -933080770, 0x5BC794E1 ^ CryptoVault.mask()) + this.editName + "\"", f4 - f9 * 2.0f - 60.0f * f6, 13.0f * f6), UiDrawCommand.TextWeight.SEMIBOLD);
        float f10 = UiKit.buttonWidth(ConfigsView.nw("\ua4ea\u54e2\u83c8\uc440\u1044\ua0d0\uad31", 1203733848, 0x5BC794E1 ^ CryptoVault.mask()), f6);
        UiKit.button(builder, uiHost, ConfigsView.nw("\udc53\uedbe\uc8c9\uadc3\uf87f\u3585", 604227430, 0x5BC794E1 ^ CryptoVault.mask()), f2 + f4 - f9 - f10, f3 + (float)Math.round(12.0f * f6), f10, Math.round(26.0f * f6), f6, UiKit.Style.GHOST, true, f7, f8, () -> {
            this.editName = null;
            this.armedDeleteName = null;
        });
        if (info == null) {
            builder.text(f2 + f9, f3 + (float)Math.round(48.0f * f6), 10.0f * f6, uiHost.muted2Color(), ConfigsView.nw("\u4e1e\u33aa\u86f0\u0718\u6b8e\ue4a9\ufc9e\ud5ae\u45e4\udbf1\u491b\u7e14\u4a3d\u9be7\u8575\ud207\u246a\u285c\ucafd\uf7e6\u718b\ub948\ud324\u437f\u6ed2\u2cd6\ub7d9\udd56\u3813\ub0db\ucdf8\u76f7\u026f\ufd55\u556e\uca97\uc357\ufaa5\ue7c2\u8e45\ucb5a\ud6e5\ub875\ue1cf\u3b9b\u6bca\uf732\u5575\ub0b8\u7105\uf480\ud822\u6859\u8102\ub307\u204e\u101f\ud6c7\ucf88\uacf2\ua2d5\u5ad7\uc0b4\u44f1\u827a\ue43d\u9056\u30fd\u2009", -437354365, 0x5BC794E1 ^ CryptoVault.mask()));
            return;
        }
        float f11 = f3 + (float)Math.round(40.0f * f6);
        builder.clipPush(f2, f11, f4, Math.max(0.0f, f3 + f5 - f11 - (float)Math.round(6.0f * f6)), 0.0f);
        uiHost.beginHitClip(f2, f11, f4, Math.max(0.0f, f3 + f5 - f11 - (float)Math.round(6.0f * f6)));
        float f12 = f3 + (float)Math.round(44.0f * f6);
        float f13 = Math.round(32.0f * f6);
        float f14 = UiKit.buttonWidth(ConfigsView.nw("\uaa2c\ua5ee\ua70f\uf70b\u4a4a\u7476\uf9ea", 671206895, 0x5BC794E1 ^ CryptoVault.mask()), f6);
        this.renameField.draw(builder, uiHost, f2 + f9, f12, f4 - f9 * 2.0f - f14 - (float)Math.round(8.0f * f6), f13, f6);
        UiKit.button(builder, uiHost, ConfigsView.nw("\u033f\u0caf\uaf93\u69a1\ud311\u7a40\u55a2", 926959887, 0x5BC794E1 ^ CryptoVault.mask()), f2 + f4 - f9 - f14, f12, f14, f13, f6, UiKit.Style.SECONDARY, !this.renameField.value().trim().isEmpty(), f7, f8, () -> {
            String string = this.renameField.value().trim();
            this.backend.rename(this.editName, string);
            ClientPrefs.get().renameConfig(this.editName, string);
            ConfigManager.getInstance().onConfigRenamed(this.editName, string);
            this.editName = string;
            this.renameField.setValue("");
        });
        float f15 = f12 + f13 + (float)Math.round(12.0f * f6);
        float f16 = Math.round(30.0f * f6);
        String string2 = info.isPublic() ? ConfigsView.nw("\uf815\u0dc6\u1d36\udd00\u32b5\u8765\u5a32\ud84a\uecac\ufc8a\u2480\ub394\ueb30\ude59\u2f0d\u6d38", 769171353, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\ud44e\u2243\uce0f\u4c90\u9b63\u1a90\u378c\ufc65\u5d23\u1000\u97a8\ua8bd\ue26a\ubfda", 1881705573, 0x5BC794E1 ^ CryptoVault.mask());
        float f17 = UiKit.buttonWidth(string2, f6);
        UiKit.button(builder, uiHost, string2, f2 + f9, f15, f17, f16, f6, UiKit.Style.SECONDARY, true, f7, f8, () -> this.backend.setPublic(this.editName, !info.isPublic()));
        float f18 = UiKit.buttonWidth(ConfigsView.nw("\ufc2c\u9d5e\u269e\u8fc0\u68ba\u32a5\u052f\ua8e2\u87fd", -1209341080, 0x5BC794E1 ^ CryptoVault.mask()), f6);
        UiKit.button(builder, uiHost, ConfigsView.nw("\u5b29\ub1dd\u170d\ub0c8\u7c92\ubd84\u0ecd\u9b18", -1024167141, 0x5BC794E1 ^ CryptoVault.mask()), f2 + f9 + f17 + (float)Math.round(8.0f * f6), f15, f18, f16, f6, UiKit.Style.DANGER, true, f7, f8, () -> {
            this.confirmDeleteName = this.editName;
            this.confirmDeleteInfo = info;
        });
        float f19 = f15 + f16 + (float)Math.round(10.0f * f6);
        String string3 = ConfigsView.nw("\u912a\ud56d\udf01\ub4dd\u93f6\u484b\u6348\u6a8e\ub386\u2cbd\u36b3\u4b6d\u63ea\uc227\u3e1f\u9513\u5e48", 447500185, 0x5BC794E1 ^ CryptoVault.mask());
        float f20 = UiKit.buttonWidth(string3, f6);
        String string4 = this.editName;
        UiKit.button(builder, uiHost, string3, f2 + f9, f19, f20, f16, f6, UiKit.Style.SECONDARY, this.backend.signedIn(), f7, f8, () -> this.copyShareCode(uiHost, string4));
        List<String> list = UiKit.wrap(ConfigsView.nw("\u0bfa\u048e\u7f2d\u06a8\ub36c\ub578\ub02f\ud4c1\uc41c\u4b3c\ub431\u4b7b\u9681\u250f\u53d2\ubcf6\u2df4\u0a26\ua8d9\u4ad3\u943b\u7906\u8003\u99c0\u9efd\u80f3\uee57\u6fd1\u5d0d\u0869\udde5\u1c6e\ue516\ucf55\u2953\ucc06\ue73c\u18a5\u86ad\u99a5\u58a3\ue2b3\u2f2c\u1c2f\u4ecb\u908e\uc97a\u2ebe\u6280\u8fd0\u77bc\u4b25\u999a\u37a1\u01ad\u09be\uf9ac\u8700\u958b\u2062\ue196\ua32c\u39b1\u471a\ue80c\u0c94\u1531\u41f0\uc691", -1911885371, 0x5BC794E1 ^ CryptoVault.mask()), Math.max(0.0f, f4 - f9 * 2.0f - f20 - (float)Math.round(10.0f * f6)), 9.0f * f6, 2);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            builder.text(f2 + f9 + f20 + (float)Math.round(10.0f * f6), f19 + (float)Math.round(4.0f * f6) + (float)(i2 * Math.round(12.0f * f6)), 9.0f * f6, uiHost.muted2Color(), list.get(i2));
        }
        float f21 = f19 + f16 + (float)Math.round(14.0f * f6);
        builder.text(f2 + f9, f21, 9.0f * f6, uiHost.muted2Color(), ConfigsView.nw("\u15c8\u6662\u7463\u6590\uff9c\u3187\u2ff1\u010f\u095c\u9a60\u9c7d\u03c8\udeb2\u24b4\u61b6\u3671\u7597\ueb79\ub712\u457b", 1255726001, 0x5BC794E1 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        float f22 = f21 + (float)Math.round(14.0f * f6);
        String string5 = ServerAutoConfig.currentServerAddress(class_310.method_1551());
        List<String> list2 = ClientPrefs.get().serversFor(this.editName);
        if (string5 == null) {
            builder.text(f2 + f9, f22 + (float)Math.round(3.0f * f6), 9.5f * f6, uiHost.mutedColor(), UiKit.fit(ConfigsView.nw("\ud5b4\u6005\u5133\u5163\ubc9d\u821a\u068c\uf89d\uc32b\uc327\ufe72\ude86\ub62a\u9d02\u038a\u2876\ufde5\u5b23\ud6d8\ufa47\u2949\u0ab5\uc077\u1332\u130a\ue35c\u9995\u18a5\u7e56\u1b25\uec2c\uac23\ud238\ue8f3\ua084\u1f01\u1c02\ude09\u8501\u77d9\ue6bf\u2b6f\ue561\u21b5\ub853\u342b\u31d7\u1d82\u6229\u4175\u0a9a\ud1f8\u7607\u6027\u6313\u1084\u1ba2\ubca9\u7460\ued07\uf2b0", 51955022, 0x5BC794E1 ^ CryptoVault.mask()), f4 - f9 * 2.0f, 9.5f * f6));
        } else {
            String string6 = ClientPrefs.normalizeAddress(string5);
            boolean bl = this.editName.equalsIgnoreCase(String.valueOf(ClientPrefs.get().serverConfigFor(string5)));
            String string7 = this.editName;
            UiKit.toggle(builder, uiHost, f2 + f9, f22, f6, bl, () -> ClientPrefs.get().setServerConfig(string5, bl ? null : string7));
            builder.text(f2 + f9 + (float)Math.round(42.0f * f6), GpuUiFontMetrics.topInBox(f22, Math.round(18.0f * f6), 10.0f * f6), 10.0f * f6, uiHost.textColor(), UiKit.fit(ConfigsView.nw("\u2bd2\u2be2\u0dd7\u66ef\uf0ac\u97df\u0164\u8e7d\uf277\ub5ca\u46e7\ua515\u17c7\u247d\uef45\u34c6\u793f\u23aa\u6d20\u9d54\ua08a\u1c5f", 867867091, 0x5BC794E1 ^ CryptoVault.mask()) + string6, f4 - f9 * 2.0f - 42.0f * f6, 10.0f * f6));
        }
        float f23 = f22 + (float)Math.round(24.0f * f6);
        if (!list2.isEmpty()) {
            String string8 = ConfigsView.nw("\u18db\u76dc\u48e8\ub25e\u2e47\uf2af\ue71d\u88db\ud43e\u81bc\u1c27", -333070376, 0x5BC794E1 ^ CryptoVault.mask());
            float f24 = UiKit.buttonWidth(string8, f6);
            builder.text(f2 + f9, f23 + (float)Math.round(4.0f * f6), 9.0f * f6, uiHost.mutedColor(), UiKit.fit(ConfigsView.nw("\ue8b4\ud763\ue0c5\u420e\u5763\u9bcb\ud049\u5bce\ufc7b", 246302704, 0x5BC794E1 ^ CryptoVault.mask()) + String.join((CharSequence)", ", list2), f4 - f9 * 2.0f - f24 - (float)Math.round(8.0f * f6), 9.0f * f6));
            String string9 = this.editName;
            UiKit.button(builder, uiHost, string8, f2 + f4 - f9 - f24, f23 - (float)Math.round(3.0f * f6), f24, Math.round(22.0f * f6), f6, UiKit.Style.GHOST, true, f7, f8, () -> ClientPrefs.get().renameConfig(string9, null));
            f23 += (float)Math.round(24.0f * f6);
        }
        float f25 = f23 + (float)Math.round(6.0f * f6);
        builder.text(f2 + f9, f25, 9.0f * f6, uiHost.muted2Color(), ConfigsView.nw("\ud150\u1791\ub7de\ud61c\ub7af\u847e\u47ec\u0c71\u9e5d", 1742605467, 0x5BC794E1 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        if (!this.editName.equals(this.editTagsFor)) {
            this.editTags.clear();
            this.editTags.addAll(info.tags());
            this.editTagsFor = this.editName;
        }
        this.drawTagToggles(builder, uiHost, f2 + f9, f25 + (float)Math.round(14.0f * f6), f4 - f9 * 2.0f, f6, f7, f8, this.editTags, string -> {
            if (!this.editTags.remove(string)) {
                this.editTags.add((String)string);
            }
            this.backend.setTags(this.editName, new ArrayList<String>(this.editTags));
        });
        uiHost.endHitClip();
        builder.clipPop();
    }

    private void copyShareCode(UiHost uiHost, String string) {
        if (!this.backend.signedIn()) {
            this.backend.setStatus(ConfigsView.nw("\ub904\u399b\u81a2\u7410\uca31\u2ff3\u915e\u218d\u987c\u861e\ua35c\u1b42\u4585\u7d9e\ud855\ua7a1\u916f\ue776\ub34a\u1bb6\ud693\udf27\u3b2f\u2d17\u3529\u1ac3\uceec\u191c", 1620603811, 0x5BC794E1 ^ CryptoVault.mask()));
            return;
        }
        this.backend.setStatus(ConfigsView.nw("\uc3a9\u8350\u7efa\u53b0\ue738\u3278\ubcd8\u0ad1\u965f\uc89a\u3f5a\ue87f\ud743\uda31\u1660\u8879\ua526\u9959\u1198\u7384\ueaba\u8d1d\ud88f\u6866\u70f1\uca16\u436f\uf3c3\uaad7\u3967", 1898671002, 0x5BC794E1 ^ CryptoVault.mask()) + string + ConfigsView.nw("\ud97e\u39fd\u7812\uff48\u9f07\u4583", -869682964, 0x5BC794E1 ^ CryptoVault.mask()));
        PrivateConfigManager.getInstance().generateShareCode(string, string2 -> {
            uiHost.setClipboard((String)string2);
            this.backend.setStatus(ConfigsView.nw("\u76d8\ufa89\u5f7c\u7cad\u7f06\u10fd\u5038\uf3fb\u6025\u900a\u2b23\uda72\ua4b1\u6977\uc2b2\u3889\ua3db", -103073829, 0x5BC794E1 ^ CryptoVault.mask()) + string + ConfigsView.nw("\u8ba4\udc95\uc30e\ua242\u8aef\ueeed\u54e9\udc73\ue51d\u359c\uef66\u6b39\ubba1\ubf0a", 1850857434, 0x5BC794E1 ^ CryptoVault.mask()) + string2 + ConfigsView.nw("\u0dfb\ue521\ub3b9\u8857\u0a89\ubb5a\u5b58\u32c2\ub57e\u6700\u9c03\u4fb0\ufe29\u9586\u87bb\ua9a4\u4f91\ue4cf\u8133\u164e\ue656\u193c\u846b\u2a3c\ud822\udfe2\ucece\ue122\udeef", 435276006, 0x5BC794E1 ^ CryptoVault.mask()));
        }, string2 -> this.backend.setStatus(ConfigsView.nw("\uf3b5\u3cb4\uc484\u8eed\uffc2\u96f9\u8599\u5fbd\u3789\uff6b\uacdc\ua378\u72e5\u538a\u7ec1\u7cdc\u546d\u43aa\u87ea\u846e\u1798\u82e0\u23ed\u1366\ubdb0\u74fd\udd50\u4171\ueec8\uda21\u7efb\ude33\u35d5\uf856\u0665\u6e11\u00cb\uffba\u0576\uae4d\u9fa7\u8f31\uf349\u7c57\uefa2\u59a3\u8981\u05a4\uc320\u1896\u4be5\u42da\ua65c\uc351\u294a\u5b7a\uf2e9\u7dea\u0b7c", -1042779382, 0x5BC794E1 ^ CryptoVault.mask()).equals(string2) ? string2 : ConfigsView.nw("\ue725\u0b9e\ucda4\ud23b\u14d4\ua9f5\u7d54\ue948\u6c72\u6358\u505e\ud2da\uf9b5\ua2d9\u5d8f\u09ac\u197d\u3555\uedcd\uc2d8\u866c", -1893274708, 0x5BC794E1 ^ CryptoVault.mask()) + string + ConfigsView.nw("\u70d0\u0be4\u89c4\ube3f\u2252\u07cb", 73306864, 0x5BC794E1 ^ CryptoVault.mask()) + (string2 == null || string2.isBlank() ? ConfigsView.nw("\u3bd5\u9386\u1a1f\uae19\ucb64\u69d6\u99a4\uea16\u243c\u4fb2\ue3d6\u862b\u0f65", -664692155, 0x5BC794E1 ^ CryptoVault.mask()) : string2)));
    }

    private void importShareCode() {
        String string2 = this.importCode.value().trim();
        if (string2.isEmpty()) {
            this.backend.setStatus(ConfigsView.nw("\u2a50\u5194\u9b00\ubc94\ue700\ub630\uf25d\u57f6\u010d\u8866\u28c2\uec7d\u2389\u664d\ua4a6\uffac\u203c\ub87f\u64fd\u709c\u9aa2\u533c\u43e3\u99bd\uf9ed\u7f13\u672a\u1148\u6954\u473b\u5263\u68da\u08e1\ud229\u5884\ud5d8\u3651", -2019670073, 0x5BC794E1 ^ CryptoVault.mask()));
            return;
        }
        if (!this.backend.signedIn()) {
            this.backend.setStatus(ConfigsView.nw("\ua575\u31fd\u024d\ubd03\ud36b\u338c\u583f\u88aa\uec67\u942d\uaecd\ud1db\u9089\ucf11\u0ae4\u0986\uac1a\ude15\ub5f1\u0faa\u95b9\u8edd\u0e74\u163d\u211f\uee63\u227e\u70ce\ud8bb\u24ac\u5418\uaf7a\u04d1\ub95b\u1ef5\u107d\u6b30", -2025768534, 0x5BC794E1 ^ CryptoVault.mask()));
            return;
        }
        if (!PrivateConfigManager.isShareCode(string2)) {
            this.backend.setStatus(ConfigsView.nw("\u3769\ue619\u481a\u6a22\u4687\u3e91\ub613\u1944\ud08a\u646b\u8185\ua9ab\u27a3\ud988\u525e\ua214\u2a9c\u0793\ud57a\ub8e5\u2998\u88d1\u7f4f\u6ad9\ue36c\u6adf\u44e4\udd48\uc1bf\ue291\u98a0\ufc5e\u93b4\u106f\u6bfb\u21cc\uc53e\uaf8a\uee9d\u32f5\ub940\uee6c\u8e88\ub6e8\u15c0\uc83b\u5cc3\u1eeb\u88bf\u39d7\uc11f\u4150\uda4e\uff96\u3b8d\u8e7f\ua6f6\u0a47\uea04\u8cfa\ubbc2\u2f91\ub112\ue99a\u3645\u3b16\uc5a0\ue330", -1157755357, 0x5BC794E1 ^ CryptoVault.mask()));
            return;
        }
        this.backend.setStatus(ConfigsView.nw("\u09f6\u47c5\u9d84\uce2e\u7278\uecd2\u29ef\u32a9\ue73c\ud077\u3d77\u5d89\udb0b\u7fef\u59aa\ue1ce\u2afe\u3505\u7e18\u7585\uc6dd\ubee1\u77f3\u4915\u9a20\u5a99\u4faf\uf62a\ub206\u2bad", -263608008, 0x5BC794E1 ^ CryptoVault.mask()));
        PrivateConfigManager.getInstance().importByShareCode(string2, this.keepOriginalKeybinds, string -> {
            this.importCode.setValue("");
            this.backend.setStatus(ConfigsView.nw("\u6bd5\u212d\uc3e7\ua691\uffcf\u8375\u29ad\u5add\u9bce\u58a1\ud822\u56b0\u3910\u0b3c\u5a4e\ub3f9\ub8e5\u3d30\u110c\u6a89\u6a95\ud607\uc82e\u9dd2\u503c", 1776917779, 0x5BC794E1 ^ CryptoVault.mask()) + string + "\".");
            this.backend.closeMenu();
        }, string -> this.backend.setStatus(ConfigsView.nw("\u3b4a\ue95f\u3581\u50cb\u348f\u8a54\u50cd\u6928\u2b7d\ue66c\ua419\u7fd2\u6768\u5fd1\uf2a2\u8201", -1074627488, 0x5BC794E1 ^ CryptoVault.mask()) + string));
    }

    private void drawHubPointer(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = Math.round(18.0f * f5);
        float f9 = Math.round(14.0f * f5);
        float f10 = Math.round(30.0f * f5);
        List<String> list = UiKit.wrap(ConfigsView.nw("\u8970\u074d\u4dab\u8cc9\u03d9\u3994\ub834\u966f\ua847\u3703\u6cb5\uc523\u8a95\u1862\ua683\u0dfc\u8c85\u7f21\u68b9\u282d\u8732\uba1d\uc042\uf80c\u1184\u2e75\ucbeb\u2571\u4f1b\u526b\u41a8\udd16\u60f4\u7e75\u4f07\u4282\u4115\uf1fa\u3345\ub350\u40f3\uada8\u65d5\u2daf\uaf8a\ub8b7\uc671\u6e2e\uaefa\u3cac\u6f16\u1a6d\u443c\u4f4e\ue75e\u23d5\u7db0\uf70b\ue27b\u60c8\u297a\u9d12\u0906\u461b\ua403\u6131\u8299\ucbb4\u8ac7\ufce3\u5cb9\u1697\u21eb\u0b53\u11e8\u1472\u3e6f\u17b4\u7539\u92db\u1e17\ub060\ue22d\uad5e\ue1cd\u4c70\u2381\udcb0\u66b8\uc271\u2bad\u650d\u2c5d\uf38a\u2663\u5a0e\u6f8f\u232c\u32bb\u3db7\u2a8d\u8f25\u92ad\u0c2c\uec80\ua96e\ub7a3\u414f\u959c\uc17b\uaaef\u1b28\u8a7e\u95a2\u3a75\ufd09\uddf5\u87ea\ue1c1\u8238\u2cb2\ua635\u5eb0\udc3d\u396d\uf360\u870f\ud517\u0950\ub1f9\u0fff\udfa2\u91e0\u342c\ue42b\ud0ec\u348d\ub7c1\u7841\u841d\ub847\uce46\uf376\ucaaa\ubb50\ua7cd\u7843\ufe2c\ue521\u8acf\u398b\u503a\u580f\uc2ee\u797a\uef86\u5b8f\u2594\u4dcc\u783b\uf0ba\u7784\u98fe\u4ecc\u642e\u8c07\ud74a\uaf8e\udfba\u3b2a\u2788\u3fc7\u9d9c\ua308\u1406", 1304065033, 0x5BC794E1 ^ CryptoVault.mask()), f4 - f8 * 2.0f, 9.5f * f5, 3);
        float f11 = Math.round(36.0f * f5);
        float f12 = f3 + f11 + (float)list.size() * f9 + (float)Math.round(10.0f * f5);
        float f13 = f12 + f10 + (float)Math.round(12.0f * f5);
        float f14 = f13 + f10 + (float)Math.round(16.0f * f5) - f3;
        UiKit.card(builder, uiHost, f2, f3, f4, f14, f5);
        builder.text(f2 + f8, f3 + (float)Math.round(16.0f * f5), 13.0f * f5, uiHost.textColor(), ConfigsView.nw("\u9c72\u34ba\u1759\u3407\u46c1\ua8e8\u848e\ue354\u49a5\u3828\u06a3\u356f\u4751\u1bf4\u94fc\u0012\uc715", 831014813, 0x5BC794E1 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            builder.text(f2 + f8, f3 + f11 + (float)i2 * f9, 9.5f * f5, uiHost.muted2Color(), list.get(i2));
        }
        float f15 = UiKit.buttonWidth(ConfigsView.nw("\ud296\uf164\uffdb\ubcd5\u6035\u1235\ub886\u6863\u2b75\u3557\ua8f5\u8ac9\u1f07\u9944\u60b1\u8ce7\u3362\udb72\u939c\u70f5\u70f3\ue3f2", 971066065, 0x5BC794E1 ^ CryptoVault.mask()), f5);
        UiKit.button(builder, uiHost, ConfigsView.nw("\u9964\udd57\u2f05\u4ff6\u9112\ud396\u25f6\ue6b0\ua969\u6c7c\u7129\u1755\uee14\u26ab\u3076\u10cb\u2a75\u1533\u02f5\uc0dc\u24eb\ue07c", 1383812139, 0x5BC794E1 ^ CryptoVault.mask()), f2 + f8, f12, f15, f10, f5, UiKit.Style.SECONDARY, true, f6, f7, () -> {
            this.tab = Tab.COMMUNITY;
        });
        float f16 = UiKit.buttonWidth(ConfigsView.nw("\u13cf\u807a\ua1d5\u1852\u217d\u2b71\uafef\ub85f", -1528668424, 0x5BC794E1 ^ CryptoVault.mask()), f5) + (float)Math.round(8.0f * f5);
        this.importCode.draw(builder, uiHost, f2 + f8, f13, f4 - f8 * 2.0f - f16 - (float)Math.round(8.0f * f5), f10, f5);
        UiKit.button(builder, uiHost, ConfigsView.nw("\uaa8e\u5c9f\ud38a\u23e8\udd96\u2e84\uf62e\u32cc", -697274860, 0x5BC794E1 ^ CryptoVault.mask()), f2 + f4 - f8 - f16, f13, f16, f10, f5, UiKit.Style.PRIMARY, !this.importCode.value().trim().isEmpty(), f6, f7, this::importShareCode);
    }

    /*
     * Unable to fully structure code
     */
    private void drawCommunity(UiDisplayList.Builder var1_1, UiHost var2_2, float var3_3, float var4_4, float var5_5, float var6_6, float var7_7, float var8_8, float var9_9) {
        if (!this.hubLoaded && !this.hubLoading) {
            this.refresh();
        }
        var10_10 = Math.round(20.0f * var7_7);
        var11_11 = Math.round(Math.min(440.0f * var7_7, var5_5 * 0.5f));
        var12_12 = var3_3 + var11_11 + var10_10;
        var13_13 = var5_5 - var11_11 - var10_10;
        var14_14 = Math.round(32.0f * var7_7);
        var15_15 = UiKit.buttonWidth(this.hubLoading != false ? ConfigsView.nw("\u1fef\u0726\uc417\uc4c7\u2823\u307a\u6fef\ub1d2\u097c", 315907756, 1539806433 ^ CryptoVault.mask()) : ConfigsView.nw("\u27f9\ub3af\u9541\ufe81\ue4ec\u5b2d\u3a3c\u078e\uaa71", -975671791, 1539806433 ^ CryptoVault.mask()), var7_7);
        this.hubSearch.draw(var1_1, var2_2, var3_3, var4_4, var11_11 - var15_15 - (float)Math.round(8.0f * var7_7), var14_14, var7_7);
        UiKit.button(var1_1, var2_2, this.hubLoading != false ? ConfigsView.nw("\u0089\ua15d\ubcaa\ua88f\ua4ee\ude93\ubf8b\ud2dc", -1500741241, 1539806433 ^ CryptoVault.mask()) : ConfigsView.nw("\u1f63\u372f\u81ea\ub542\u972a\ud2f1\ue97e\u34f3\u18bb\u643b", 2132547607, 1539806433 ^ CryptoVault.mask()), var3_3 + var11_11 - var15_15, var4_4, var15_15, var14_14, var7_7, UiKit.Style.SECONDARY, this.hubLoading == false, var8_8, var9_9, (Runnable)LambdaMetafactory.metafactory(null, null, null, ()V, refresh(), ()V)((ConfigsView)this));
        var16_16 = var4_4 + var14_14 + (float)Math.round(8.0f * var7_7);
        var17_17 = Math.round(26.0f * var7_7);
        UiKit.segmented(var1_1, var2_2, ConfigsView.SORT_LABELS, this.sortIndex, var3_3, var16_16, var11_11, var17_17, var7_7, var8_8, var9_9, (IntConsumer)LambdaMetafactory.metafactory(null, null, null, (I)V, lambda$drawCommunity$0(int ), (I)V)((ConfigsView)this));
        var18_18 = var16_16 + var17_17 + (float)Math.round(10.0f * var7_7);
        var19_19 = this.drawTagToggles(var1_1, var2_2, var3_3, var18_18, var11_11, var7_7, var8_8, var9_9, this.filterTags, (Consumer<String>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$drawCommunity$1(java.lang.String ), (Ljava/lang/String;)V)((ConfigsView)this));
        var20_20 = this.hubSearch.value().trim().toLowerCase(Locale.ROOT);
        var21_21 = new ArrayList<RemoteConfig>();
        for (RemoteConfig var23_24 : this.hub) {
            v0 = var24_26 = var20_20.isEmpty() != false || ConfigsView.contains(var23_24.name, var20_20) != false || ConfigsView.contains(var23_24.author, var20_20) != false || ConfigsView.contains(var23_24.gameMode, var20_20) != false || ConfigsView.contains(var23_24.description, var20_20) != false;
            if (this.filterTags.isEmpty()) ** GOTO lbl-1000
            if (var23_24.tags != null) {
                ** if (!var23_24.tags.stream().anyMatch((Predicate<String>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)Z, contains(java.lang.Object ), (Ljava/lang/String;)Z)(this.filterTags))) goto lbl-1000
            }
            ** GOTO lbl-1000
lbl-1000:
            // 2 sources

            {
                v1 = true;
                ** GOTO lbl30
            }
lbl-1000:
            // 2 sources

            {
                v1 = var25_28 = false;
            }
lbl30:
            // 2 sources

            if (!var24_26 || !var25_28) continue;
            var21_21.add(var23_24);
        }
        var22_23 = var18_18 + var19_19 + (float)Math.round(12.0f * var7_7);
        var23_25 = Math.max(0.0f, var6_6 - (var22_23 - var4_4));
        this.hubRect[0] = var3_3;
        this.hubRect[1] = var22_23;
        this.hubRect[2] = var11_11;
        this.hubRect[3] = var23_25;
        var24_27 = Math.round(78.0f * var7_7);
        var25_29 = Math.round(8.0f * var7_7);
        var26_30 = var24_27 + var25_29;
        this.hubScroll.update(var2_2.frameDelta(), var23_25, Math.max(0.0f, (float)var21_21.size() * var26_30 - var25_29), var2_2.now());
        var27_31 = this.hubScroll.offset();
        if (this.hubError != null) {
            UiKit.emptyState(var1_1, var2_2, ConfigsView.nw("\u7d0b\udd20\u15f2\u1342\ud3df\u384f\uc65e\u7033\u2395\u92f6\u9170\u47d3\uc9ca\u2850\ua8d1\uedb0\uea32\ue39d\uec4f\u2c6f\ub2ff\ua32b\u3b41\ua0f6\ucb97\uab29", 802498340, 1539806433 ^ CryptoVault.mask()), UiKit.fit(this.hubError, var11_11, 10.0f * var7_7), var3_3, var22_23 + (float)Math.round(36.0f * var7_7), var11_11, var7_7);
        } else if (var21_21.isEmpty()) {
            UiKit.emptyState(var1_1, var2_2, this.hubLoading != false ? ConfigsView.nw("\u07e5\u1082\u0101\u482d\uadaa\u3ab8\uba41\ua558\u161c\u27e6\u0c75\ue7c3\uadcf\u21ca\ubfd9\u9128\u0b6f\ub574\u6b63\u1e98\u9693\u7eae\u868d\udc09\u2ca0\ucae8\u441e\uffdd\ue919", 339381777, 1539806433 ^ CryptoVault.mask()) : ConfigsView.nw("\u6ee5\u7727\u9f2e\ucb94\u5ec9\u5345\u1d54\ua254\u50eb\uefe9\u4f95\ud4ab\ud1e4\u2e34\u36e5\u1049\uee37", -1200988772, 1539806433 ^ CryptoVault.mask()), this.hubLoading != false ? "" : (var20_20.isEmpty() != false && this.filterTags.isEmpty() != false ? ConfigsView.nw("\u2fb0\ua298\u18ce\u1e36\u0a5f\u3219\u6213\ufcbf\ubdca\ud1a3\u32d6\u2f7d\u9bad\u6d5f\ua387\uc44d\u476f\uceb0\ubfe0\u1ffa\u80ec\u7429\u5511\u6136\u88f8\u5095\u8e3b\u4ece\ubf81\u5b1b\ufd36\u6d58\u35d3\ufd76", 1034327858, 1539806433 ^ CryptoVault.mask()) : ConfigsView.nw("\ufc10\u247f\u315a\u8cc90\u2da0\u9094\u5f59\u3a6a\u4cd1\u4fa9\u93dd\u8606\u2a0d\u9f56\u9151\ue587\u7d60\uefd0\ub7d7\ud4be\u8105\ua9e3\u61b3\uff54\u5ad0\u6e6b\u4106\u775e\u2430\u47b4\u1015\uc279", -1313538371, 1539806433 ^ CryptoVault.mask())), var3_3, var22_23 + (float)Math.round(36.0f * var7_7), var11_11, var7_7);
        }
        var1_1.clipPush(var3_3, var22_23, var11_11, var23_25, 0.0f);
        var2_2.beginHitClip(var3_3, var22_23, var11_11, var23_25);
        for (var28_32 = 0; var28_32 < var21_21.size(); ++var28_32) {
            var29_33 = var22_23 + (float)var28_32 * var26_30 - var27_31;
            if (var29_33 + var24_27 < var22_23 || var29_33 > var22_23 + var23_25) continue;
            this.drawHubRow(var1_1, var2_2, (RemoteConfig)var21_21.get(var28_32), var3_3, var29_33, var11_11, var24_27, var7_7, var8_8, var9_9);
        }
        var2_2.endHitClip();
        var1_1.clipPop();
        this.hubScroll.paintBar(var1_1, var3_3 + var11_11 + (float)Math.round(4.0f * var7_7), var22_23, var23_25, var7_7, var2_2.textColor(), var2_2.now());
        this.drawDetails(var1_1, var2_2, var12_12, var4_4, var13_13, var6_6, var7_7, var8_8, var9_9);
    }

    private static boolean contains(String string, String string2) {
        return string != null && string.toLowerCase(Locale.ROOT).contains(string2);
    }

    private void drawHubRow(UiDisplayList.Builder builder, UiHost uiHost, RemoteConfig remoteConfig, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9;
        boolean bl = this.selected != null && remoteConfig.id != null && remoteConfig.id.equals(this.selected.id);
        boolean bl2 = uiHost.hovered(f7, f8, f2, f3, f4, f5);
        float f10 = Math.round(10.0f * f6);
        UiKit.card(builder, uiHost, f2, f3, f4, f5, f6);
        if (bl2 && !bl) {
            builder.rect(f2, f3, f4, f5, f10, uiHost.controlFill(12));
        }
        if (bl) {
            builder.rect(f2, f3, f4, f5, f10, UiKit.withAlpha(uiHost.accent(), 20));
            builder.border(f2, f3, f4, f5, f10, 1.0f, UiKit.withAlpha(uiHost.accent(), 160));
        }
        float f11 = Math.round(16.0f * f6);
        builder.text(f2 + f11, f3 + (float)Math.round(12.0f * f6), 12.5f * f6, uiHost.textColor(), UiKit.fit(remoteConfig.name, f4 - f11 * 2.0f - 70.0f * f6, 12.5f * f6), UiDrawCommand.TextWeight.SEMIBOLD);
        builder.text(f2 + f11, f3 + (float)Math.round(30.0f * f6), 9.5f * f6, uiHost.mutedColor(), UiKit.fit(ConfigsView.nw("\ufdc9\u15b3\u895e\ud807\u468f\u125d\u30c5", 1653578575, 0x5BC794E1 ^ CryptoVault.mask()) + (remoteConfig.author == null ? ConfigsView.nw("\u75a3\u16ff\u4f83\ua5a2\u6ba5\u7b15\u4458\uac89", -117747074, 0x5BC794E1 ^ CryptoVault.mask()) : remoteConfig.author), Math.max(0.0f, f4 - f11 * 2.0f - 170.0f * f6), 9.5f * f6));
        String string = remoteConfig.ratingCount > 0 ? String.format(Locale.ROOT, ConfigsView.nw("\u423f\u63a6\ue668\u9796\u1fcf\u8681\u5418\u98fa\u475a\u45b0\u5d76\u6221", 379793532, 0x5BC794E1 ^ CryptoVault.mask()), remoteConfig.ratingAvg, remoteConfig.ratingCount) : ConfigsView.nw("\u169e\u3f6e\u4c75\u673c\u8916\u59c1\u7372\u952f\u44eb", -2083076391, 0x5BC794E1 ^ CryptoVault.mask());
        float f12 = GpuUiFontMetrics.textWidth(string, 9.0f * f6);
        float f13 = ConfigsView.ratingDotsWidth(Math.round(7.0f * f6), Math.round(3.0f * f6));
        float f14 = f2 + f4 - f11 - f12;
        builder.text(f14, f3 + (float)Math.round(30.0f * f6), 9.0f * f6, uiHost.muted2Color(), string);
        this.drawRatingDots(builder, uiHost, f14 - (float)Math.round(6.0f * f6) - f13, f3 + (float)Math.round(31.0f * f6), Math.round(7.0f * f6), Math.round(3.0f * f6), remoteConfig.ratingAvg, remoteConfig.myRating, false, f7, f8, null);
        if (this.sortIndex == 0 && remoteConfig.weeklyScore > 0) {
            f9 = GpuUiFontMetrics.textWidth(ConfigsView.nw("\ud717\u8341\u41c3\uf64c\ub2e2\u5892\u0bf7\ucfcb\ue33d\ua443", -1669175270, 0x5BC794E1 ^ CryptoVault.mask()), 8.5f * f6) + 14.0f * f6;
            UiKit.chip(builder, uiHost, ConfigsView.nw("\ua111\ua6e7\u1bd8\u6a5e\u330b\u036d\u529e\u2c18\u481a\ud1ee\u58dc", -1306850424, 0x5BC794E1 ^ CryptoVault.mask()), f14 - (float)Math.round(12.0f * f6) - f13 - f9, f3 + (float)Math.round(28.0f * f6), f6, uiHost.accent());
        }
        if (remoteConfig.tags != null && !remoteConfig.tags.isEmpty()) {
            f9 = f2 + f11;
            for (int i2 = 0; i2 < remoteConfig.tags.size() && f9 < f2 + f4 - f11 - 50.0f * f6; f9 += UiKit.chip(builder, uiHost, remoteConfig.tags.get(i2), f9, f3 + (float)Math.round(48.0f * f6), f6, uiHost.mutedColor()) + (float)Math.round(5.0f * f6), ++i2) {
            }
        } else {
            String string2 = remoteConfig.description == null || remoteConfig.description.isBlank() ? ConfigsView.nw("\u8d15\ud7db\ufb74\ub612\uea11\u3ed4\udbe7\u8647\ufc1d\ud445\u7fe0\u2c60\u14b1\u8534\ub068\u8bee\ua755\ua1c5\u2a02", -1977044141, 0x5BC794E1 ^ CryptoVault.mask()) : remoteConfig.description;
            builder.text(f2 + f11, f3 + (float)Math.round(50.0f * f6), 9.5f * f6, uiHost.muted2Color(), UiKit.fit(string2, f4 - f11 * 2.0f, 9.5f * f6));
        }
        String string3 = remoteConfig.gameMode == null ? ConfigsView.nw("\u0761\ue099\u5389\u35ef\u7437\u68b1\u4e86\u4376", 1431354171, 0x5BC794E1 ^ CryptoVault.mask()) : remoteConfig.gameMode;
        float f15 = GpuUiFontMetrics.textWidth(string3, 8.5f * f6) + 14.0f * f6;
        UiKit.chip(builder, uiHost, string3, f2 + f4 - f11 - f15, f3 + (float)Math.round(12.0f * f6), f6, uiHost.accent());
        if (remoteConfig.owned) {
            float f16 = GpuUiFontMetrics.textWidth(ConfigsView.nw("\u6df8\u1392\u3536\u898c\u2320\uc19c\u164f\ud0b8\ua855", 267471845, 0x5BC794E1 ^ CryptoVault.mask()), 8.5f * f6) + 14.0f * f6;
            UiKit.chip(builder, uiHost, ConfigsView.nw("\ufec6\u31bb\u8bc0\u0b93\u3a67\u08f4\ud69e\u2238\ud315", 569320589, 0x5BC794E1 ^ CryptoVault.mask()), f2 + f4 - f11 - f15 - f16 - (float)Math.round(6.0f * f6), f3 + (float)Math.round(12.0f * f6), f6, -9247307);
        }
        uiHost.hit(f2, f3, f4, f5, () -> {
            this.selected = remoteConfig;
            this.armedDeleteId = null;
        });
    }

    private void drawDetails(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = Math.round(18.0f * f6);
        List list = this.selected == null ? List.of() : UiKit.wrap(this.selected.description == null || this.selected.description.isBlank() ? ConfigsView.nw("\u035b\u9395\u9c64\uac50\u43dc\u58d3\u19fd\u1fe0\u7c62\u73f1\u9817\u147f\uf66d\u234d\u699b\ua175\u255a\ub96a\u0b81\u8da0\uce1d\u1bfd\ub765\u8ce1\ufa47\u5cbd\ufc48\u5837\u14d6\uf5c2\u4e6d\u86ba\u1d55\u3a09\u1824\u38b6\u0101\u3969\u7b3c\udeaa", 550940250, 0x5BC794E1 ^ CryptoVault.mask()) : this.selected.description, f4 - f9 * 2.0f, 10.0f * f6, 4);
        float f10 = Math.round(32.0f * f6);
        float f11 = Math.round(24.0f * f6);
        float f12 = Math.round(26.0f * f6);
        float f13 = this.selected == null ? (float)Math.round(150.0f * f6) : Math.min(f5 * 0.66f, (float)Math.round(104.0f * f6) + f12 + (float)(list.size() * Math.round(15.0f * f6)) + f11 + (float)Math.round(20.0f * f6) + f10 + f9);
        UiKit.card(builder, uiHost, f2, f3, f4, f13, f6);
        if (this.selected == null) {
            UiKit.emptyState(builder, uiHost, ConfigsView.nw("\u1337\u9c08\u2129\ufa0b\u4f54\uda50\u35ab\u8068\uf57e\ua06e\ue1f5\u4be7\ufc90\uce2c\u80af\u3444\ub019\u347b\u74b3", 2070669447, 0x5BC794E1 ^ CryptoVault.mask()), ConfigsView.nw("\u8c46\u74f5\u3c40\ufa44\u2f7a\u2362\uca2f\u5295\ud020\u789b\u0873\ue2a1\u9458\u9619\u21b6\ub111\ue1bf\u57a2\u1d0d\ufa32\u2687\ub124\ucbfc\ubfd6\u1f27\u9306\uca26\ueffd\u43c2\u4e1e\u8580\u047b\udd04\u8157\u9652\u9707\u442e\uea7e\u8d2e\u5502\ud44f\u4262\u3af6\uefd5\uacc5", 1473268756, 0x5BC794E1 ^ CryptoVault.mask()), f2, f3 + f13 * 0.5f - (float)Math.round(10.0f * f6), f4, f6);
        } else {
            String string;
            RemoteConfig remoteConfig = this.selected;
            builder.text(f2 + f9, f3 + (float)Math.round(16.0f * f6), 17.0f * f6, uiHost.titleColor(), UiKit.fit(remoteConfig.name, f4 - f9 * 2.0f, 17.0f * f6), UiDrawCommand.TextWeight.SEMIBOLD);
            builder.text(f2 + f9, f3 + (float)Math.round(40.0f * f6), 10.0f * f6, uiHost.mutedColor(), ConfigsView.nw("\u5b45\u5fd7\ub013\u93e6", -251963662, 0x5BC794E1 ^ CryptoVault.mask()) + (remoteConfig.author == null ? ConfigsView.nw("\u8898\u323c\u4578\u5d23\udd69\uf8af\u6165\u2838\u030d\ud2b0", 852788813, 0x5BC794E1 ^ CryptoVault.mask()) : remoteConfig.author));
            float f14 = f2 + f9;
            float f15 = f3 + (float)Math.round(60.0f * f6);
            f14 += UiKit.chip(builder, uiHost, remoteConfig.gameMode == null ? ConfigsView.nw("\uff71\u6be9\ua03c\u602f\u396a\u0a7b\uc4e8\u1b0b\uce8c", -1830522199, 0x5BC794E1 ^ CryptoVault.mask()) : remoteConfig.gameMode, f14, f15, f6, uiHost.accent()) + (float)Math.round(6.0f * f6);
            if (remoteConfig.owned) {
                f14 += UiKit.chip(builder, uiHost, remoteConfig.isPublic ? ConfigsView.nw("\ua051\u0a7d\uc192\u4871\u1e95\ua56b\u8966\u40ed", 396268089, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\u692c\ud220\u18eb\u6b56\u8ad3\u1c5e\u4b60\uc56a\u134a\u3611", 1876853367, 0x5BC794E1 ^ CryptoVault.mask()), f14, f15, f6, remoteConfig.isPublic ? -9247307 : -14741) + (float)Math.round(6.0f * f6);
            }
            if (remoteConfig.tags != null) {
                for (String string2 : remoteConfig.tags) {
                    if (f14 > f2 + f4 - f9 - 50.0f * f6) break;
                    f14 += UiKit.chip(builder, uiHost, string2, f14, f15, f6, uiHost.mutedColor()) + (float)Math.round(5.0f * f6);
                }
            }
            float f16 = f3 + (float)Math.round(86.0f * f6);
            boolean bl = !remoteConfig.owned && this.backend.signedIn() && !this.rating;
            float f17 = Math.round(12.0f * f6);
            float f18 = Math.round(5.0f * f6);
            this.drawRatingDots(builder, uiHost, f2 + f9, f16 + (float)Math.round(2.0f * f6), f17, f18, remoteConfig.ratingAvg, remoteConfig.myRating, bl, f7, f8, n2 -> this.rate(remoteConfig, n2 == remoteConfig.myRating ? 0 : n2));
            String string3 = remoteConfig.ratingCount > 0 ? String.format(Locale.ROOT, ConfigsView.nw("\ucfaf\u2953\u0fcb\u9a02\ub080\u78b9\uf4d3\u111a\u0d13\uc392\u8f0c\u6ea1\uf226\ua60d\uf854\ud72c\u7f4d\ub2bc\ud7bc\uc671\u67fe\ua1d2\uf819\ucc0a\ufd15", -1149742523, 0x5BC794E1 ^ CryptoVault.mask()), remoteConfig.ratingAvg, remoteConfig.ratingCount, remoteConfig.ratingCount == 1 ? "" : "s") : (string = ConfigsView.nw("\u6dc2\u1fc6\ue1ab\ua7d4\uf792\u1586\ua3db\ub7b5\u0a02\u7706\u8cfe\ufd17\ud8c9\u82ff\u30ca\u7304\ua037\u8700", -1911952302, 0x5BC794E1 ^ CryptoVault.mask()));
            if (remoteConfig.myRating > 0) {
                string = string + ConfigsView.nw("\ubcbb\uf2ce\u6ddf\u3903\u7129\u6dfe\u0d4d\ucb88\u6783\u12f0\uc689", -1683679349, 0x5BC794E1 ^ CryptoVault.mask()) + remoteConfig.myRating;
            } else if (remoteConfig.owned) {
                string = string + ConfigsView.nw("\u06fd\u59cf\u50be\u2114\u570a\u9fb4\u84ae\u4e6d\u166e\u5126\uf796\u5b08\u7ae3\uc343\u9964", -746481923, 0x5BC794E1 ^ CryptoVault.mask());
            } else if (!this.backend.signedIn()) {
                string = string + ConfigsView.nw("\u47b3\u710a\u009f\u6682\uc5ab\u45f3\u5cec\uf3dc\u22e0\uc6dc\u5355\u2272\ua99d\u1fcc\ub172\u30af\uc9da\u8d0f\u0e93\ub091", -734837873, 0x5BC794E1 ^ CryptoVault.mask());
            }
            float f19 = f2 + f9 + ConfigsView.ratingDotsWidth(f17, f18) + (float)Math.round(10.0f * f6);
            builder.text(f19, GpuUiFontMetrics.topInBox(f16, f17 + (float)Math.round(4.0f * f6), 9.5f * f6), 9.5f * f6, uiHost.mutedColor(), UiKit.fit(string, f2 + f4 - f9 - f19, 9.5f * f6));
            for (int i2 = 0; i2 < list.size(); ++i2) {
                builder.text(f2 + f9, f3 + (float)Math.round(88.0f * f6) + f12 + (float)(i2 * Math.round(15.0f * f6)), 10.0f * f6, uiHost.mutedColor(), (String)list.get(i2));
            }
            float f20 = f3 + f13 - f9 - f10;
            float f21 = f20 - f11;
            float f22 = f21 + (float)Math.round((f11 - 18.0f * f6) * 0.5f);
            UiKit.toggle(builder, uiHost, f2 + f9, f22, f6, this.keepOriginalKeybinds, () -> {
                this.keepOriginalKeybinds = !this.keepOriginalKeybinds;
            });
            builder.text(f2 + f9 + (float)Math.round(42.0f * f6), f21 + (float)Math.round((f11 - 9.5f * f6) * 0.5f), 9.5f * f6, uiHost.mutedColor(), ConfigsView.nw("\ue31f\ued61\u7497\u66e5\u881a\u9671\uac02\u0e1e\u9894\u2774\ubdb3\u03e8\ud3ee\u92d3\u0404\ucc35\u2377\u4366\u5525\u80fa\u5e93\uda11\u3d87\ue632\u12df\ub2eb\u446d\ub3cf\ufe0d\ub1e4\u9e82\u7c57\uc0b4\ud4f6\ud222\u2626\ubefe\u9e17\ub360\u9883\ua95b\u5c66\u5bd5\ud656\u831e\u3a2e\u0c77\u3eb7\u1bf4", 1428249167, 0x5BC794E1 ^ CryptoVault.mask()));
            float f23 = f2 + f9;
            float f24 = UiKit.buttonWidth(ConfigsView.nw("\ua850\u5b07\ub904\u8c54\ub1cc\u2c87\u7056\u6d00\udbc3\u76a7\u7545\u89cb\uc050\u5c06\ub1ea\ufc1b\ub380\u9ba9\u2e15\ue231", -495178337, 0x5BC794E1 ^ CryptoVault.mask()), f6);
            UiKit.button(builder, uiHost, ConfigsView.nw("\ud461\u27cb\u9ed2\u4609\u1dd2\ue82e\uddb7\u236f\u2652\u113c\u90e4\ueaf7\u2e6d\uf45f\uecb8\uad22\u7f1f\ua578", 868045127, 0x5BC794E1 ^ CryptoVault.mask()), f23, f20, f24, f10, f6, UiKit.Style.PRIMARY, true, f7, f8, () -> this.download(remoteConfig));
            f23 += f24 + (float)Math.round(8.0f * f6);
            if (remoteConfig.owned) {
                String string4 = remoteConfig.isPublic ? ConfigsView.nw("\u2ca2\u21a9\ue14e\u4df2\u0451\u3500\ucb3a\ub255\ucd1a\uf74f\ua1fb\ub570\u2dfa\u352c\ua07f\u7f5a", -1464183888, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\u74fb\ue150\u835f\u3a3b\u6c42\u06a3\uda31\u052d\uf5ee\uc9b6\udaa6\u41df\uef3c\ufe23\u4760", 740801503, 0x5BC794E1 ^ CryptoVault.mask());
                float f25 = UiKit.buttonWidth(string4, f6);
                UiKit.button(builder, uiHost, string4, f23, f20, f25, f10, f6, UiKit.Style.SECONDARY, true, f7, f8, () -> this.toggleVisibility(remoteConfig));
                boolean bl2 = this.armedDeleteId != null && this.armedDeleteId.equals(remoteConfig.id) && uiHost.now() - this.armedAt < 3500000000L;
                String string5 = bl2 ? ConfigsView.nw("\ud099\u74f9\uf38b\udafd\uae84\u0b35\u0ab1\u0001\u3cac\ued39\uf30e\uea74\u0283\ufa15\ua3a0\uc802", -2113576763, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\u3cf2\u9f35\u4909\u8763\ud228\ub695\u9a92\u2cae\u5ea1", -2009915691, 0x5BC794E1 ^ CryptoVault.mask());
                float f26 = UiKit.buttonWidth(ConfigsView.nw("\ud433\u2355\u4db0\uc370\u2681\u357f\u6d6b\u6ff7\u9a6a\u532a\u254c\ub2af\u1731\u6fc7\u3467", 1263320681, 0x5BC794E1 ^ CryptoVault.mask()), f6);
                UiKit.button(builder, uiHost, string5, f23 += f25 + (float)Math.round(8.0f * f6), f20, f26, f10, f6, UiKit.Style.DANGER, true, f7, f8, () -> {
                    if (bl2) {
                        this.delete(remoteConfig);
                    } else {
                        this.armedDeleteId = remoteConfig.id;
                        this.armedAt = uiHost.now();
                    }
                });
            }
        }
        this.drawShare(builder, uiHost, f2, f3 + f13 + (float)Math.round(16.0f * f6), f4, f5 - f13 - (float)Math.round(16.0f * f6), f6, f7, f8);
    }

    private void drawShare(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = Math.min(f5, (float)Math.round(170.0f * f6));
        if (f9 < (float)Math.round(110.0f * f6)) {
            return;
        }
        UiKit.card(builder, uiHost, f2, f3, f4, f9, f6);
        float f10 = Math.round(18.0f * f6);
        builder.text(f2 + f10, f3 + (float)Math.round(16.0f * f6), 13.0f * f6, uiHost.textColor(), ConfigsView.nw("\ude1c\ud334\u9662\u2027\u98fb\u07bc\u7d9c\u919e\u4211\u9324\uc611\u0482\u8914\u3d42\u5d54\u134c\u2d2eZ\u072e\u9395\u976b\ua383\u68b9\u9fae\u9820\u75ee\ua5d9\ue491\u17a7", 484781823, 0x5BC794E1 ^ CryptoVault.mask()), UiDrawCommand.TextWeight.SEMIBOLD);
        List<String> list = this.backend.savedConfigs();
        if (list.isEmpty()) {
            builder.text(f2 + f10, f3 + (float)Math.round(46.0f * f6), 10.0f * f6, uiHost.muted2Color(), ConfigsView.nw("\u403a\uc52a\u68e6\ue8f9\u2f8d\ub76a\u7678\u4b7b\ua5be\u280a\u2ce6\u6eff\u5f65\u1976\u168d\u35cf\uf33b\u8c67\ub93b\udecd\u8d63\u842c\uf4d2\u28c9\u8ba4\u0b2b\u9a22\ub06e\u88ca\u7156\u2fad\uf087\u924e\u69d1\uc444\u270d\u5e82\udf44\ufc0e\u3bc0\u9483\ubb58\ud1fc\u8077\ufde3\ubcb7\u0a48\u962f\uf53d\uc201\ud788\u161b", 1921505954, 0x5BC794E1 ^ CryptoVault.mask()));
            return;
        }
        this.shareIndex = Math.floorMod(this.shareIndex, list.size());
        String string = list.get(this.shareIndex);
        float f11 = f3 + (float)Math.round(42.0f * f6);
        float f12 = Math.round(32.0f * f6);
        float f13 = Math.round(30.0f * f6);
        UiKit.button(builder, uiHost, "<", f2 + f10, f11, f13, f12, f6, UiKit.Style.SECONDARY, list.size() > 1, f7, f8, () -> --this.shareIndex);
        float f14 = UiKit.buttonWidth(ConfigsView.nw("\ubb6e\u0160\ua944\u9381\u6943\ufa6e\ua203\uf111\ucaef", -1942954504, 0x5BC794E1 ^ CryptoVault.mask()), f6) + (float)Math.round(8.0f * f6);
        float f15 = f4 - f10 * 2.0f - f13 * 2.0f - (float)Math.round(12.0f * f6);
        float f16 = f2 + f10 + f13 + (float)Math.round(6.0f * f6);
        builder.rect(f16, f11, f15, f12, Math.min(f12 * 0.5f, (float)Math.round(12.0f * f6)), uiHost.controlFill(16));
        String string2 = UiKit.fit(string, f15 - 16.0f * f6, 11.0f * f6);
        builder.text(f16 + (f15 - GpuUiFontMetrics.textWidth(string2, 11.0f * f6)) * 0.5f, GpuUiFontMetrics.topInBox(f11, f12, 11.0f * f6), 11.0f * f6, uiHost.textColor(), string2, UiDrawCommand.TextWeight.SEMIBOLD);
        UiKit.button(builder, uiHost, ">", f2 + f4 - f10 - f13, f11, f13, f12, f6, UiKit.Style.SECONDARY, list.size() > 1, f7, f8, () -> ++this.shareIndex);
        float f17 = f11 + f12 + (float)Math.round(10.0f * f6);
        this.shareDescription.draw(builder, uiHost, f2 + f10, f17, f4 - f10 * 2.0f - f14 - (float)Math.round(8.0f * f6), f12, f6);
        UiKit.button(builder, uiHost, ConfigsView.nw("\ue75e\ua391\u9dfa\u5370\u6c37\u2975\u40c7\u1547", -618916110, 0x5BC794E1 ^ CryptoVault.mask()), f2 + f4 - f10 - f14, f17, f14, f12, f6, UiKit.Style.PRIMARY, this.backend.signedIn(), f7, f8, () -> this.share(string));
        List<String> list2 = UiKit.wrap(ConfigsView.nw("\u7292\u11e4\uc4ac\ueffd\ud0af\ua22d\ud511\uc77c\u3498\udb9c\ubf3a\u1deb\u3041\ud727\u7be8\u9524\u5e22\u1db0\u956f\ueba7\uf0dc\u413c\u2aff\ucfc8\u2c7e\ub4c2\uf88a\ue365\uacec\u1f3c\u5950\u832b\u3348\ucee6\u28ce\u150c\u40c4\u3efe\u129b\u0e02\u2a96\uf427\u8907\u8e1a\udc14\u0ac4\uabee\u5951\u646d\ue4bf\u6377\u2c49\u9d5b\ua93b\u45a3\u0452\u9897\u3103\uded3\u431e\ue372\ubdb4\u477d\ucf1d\ub62d\u7e9f\u6568\uad0f\u042b\u61ed\u65bc\ubc26\ud831\ua671\uc850\ube42\ue204\u575d\u9da9\ud7cc\u403f\uebe9\u294b\u3da9", 443273970, 0x5BC794E1 ^ CryptoVault.mask()), f4 - f10 * 2.0f, 9.0f * f6, 2);
        for (int i2 = 0; i2 < list2.size(); ++i2) {
            builder.text(f2 + f10, f17 + f12 + (float)Math.round(10.0f * f6) + (float)(i2 * Math.round(13.0f * f6)), 9.0f * f6, uiHost.muted2Color(), list2.get(i2));
        }
    }

    private static float ratingDotsWidth(float f2, float f3) {
        return f2 * 5.0f + f3 * 4.0f;
    }

    private void drawRatingDots(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float f5, double d2, int n2, boolean bl, float f6, float f7, IntConsumer intConsumer) {
        int n3;
        int n4 = 0;
        if (bl) {
            for (n3 = 1; n3 <= 5; ++n3) {
                float f8 = f2 + (float)(n3 - 1) * (f4 + f5);
                if (!uiHost.hovered(f6, f7, f8 - f5 * 0.5f, f3 - f5, f4 + f5, f4 + f5 * 2.0f)) continue;
                n4 = n3;
            }
        }
        n3 = n4 > 0 ? n4 : (n2 > 0 ? n2 : (int)Math.round(d2));
        int n5 = n4 > 0 || n2 > 0 ? uiHost.accent() : -340971;
        for (int i2 = 1; i2 <= 5; ++i2) {
            float f9 = f2 + (float)(i2 - 1) * (f4 + f5);
            boolean bl2 = i2 <= n3;
            builder.rect(f9, f3, f4, f4, f4 * 0.5f, bl2 ? n5 : uiHost.controlFill(38));
            if (!bl2) {
                builder.border(f9, f3, f4, f4, f4 * 0.5f, 1.0f, 0x22FFFFFF);
            }
            if (!bl || intConsumer == null) continue;
            int n6 = i2;
            uiHost.hit(f9 - f5 * 0.5f, f3 - f5, f4 + f5, f4 + f5 * 2.0f, () -> intConsumer.accept(n6));
        }
    }

    private void rate(RemoteConfig remoteConfig, int n2) {
        if (this.rating) {
            return;
        }
        this.rating = true;
        this.backend.setStatus(n2 == 0 ? ConfigsView.nw("\u454d\u5900\u6b67\u56de\u79f6\u6eb5\ua034\u2bf5\u6491\ufe73\u8a22\ucdf9\u1169\u4d2e\u38ef\u4811\u7990\u05ca\uf67a\u8696\uf7ef\uf452\ub63a\u9542\u471c", 124730009, 0x5BC794E1 ^ CryptoVault.mask()) : ConfigsView.nw("\u7add\u6078\u2f3f\ucb34\u6987\u0e67\u62f7\uae86\u39b1\u1c4e", -426157994, 0x5BC794E1 ^ CryptoVault.mask()) + remoteConfig.name + ConfigsView.nw("\uaf4c\ua611\u1c1c\u88aa\u3a8b\u173e\u87f9\u0234", 1542722545, 0x5BC794E1 ^ CryptoVault.mask()));
        this.manager.rate(remoteConfig, n2, string -> {
            this.rating = false;
            this.backend.setStatus((String)string);
        }, string -> {
            this.rating = false;
            this.backend.setStatus((String)string);
        });
    }

    private void download(RemoteConfig remoteConfig) {
        this.backend.setStatus(ConfigsView.nw("\u169d\u09a2\ua604\u797e\u4850\u248e\ue24c\u72ca\u7685\u2b8c\u5ff4\ue05c\uf793\u79f9", 2070363507, 0x5BC794E1 ^ CryptoVault.mask()) + remoteConfig.name + ConfigsView.nw("\u94d9\u6684\u5d56\u1469\u4120\u5d94", 1783048624, 0x5BC794E1 ^ CryptoVault.mask()));
        this.manager.downloadConfig(remoteConfig, this.keepOriginalKeybinds, string -> {
            this.backend.setStatus(ConfigsView.nw("\udea4\u10bb\u1bd6\u5daf\u6375\u705d\ue718\ua6a7\u49a1\u9047\u64f6\u6d18\ufb3c\u3f09\u3b5f\u0a75\u719f\u3706\ubecf\ue45c\u67a4\u79db\ue5b9\ufca8\ua2f1", -1603874035, 0x5BC794E1 ^ CryptoVault.mask()) + remoteConfig.name + "\".");
            this.backend.closeMenu();
        }, string -> this.backend.setStatus(ConfigsView.nw("\u1fb8\ub54f\u9569\u087c\u4abb\u3f41\u858c\uae93\uc632\u8fbd\ucdf9\ua756\u1ef7\u3c3b\u8516\uc856\u2b63\u5a08\u9b0d\udeb1\u5e35", -1095507286, 0x5BC794E1 ^ CryptoVault.mask()) + string));
    }

    private void toggleVisibility(RemoteConfig remoteConfig) {
        boolean bl = !remoteConfig.isPublic;
        this.backend.setStatus(ConfigsView.nw("\uc765\u5111\u2577\uee5b\u7641\uc540\u4c68\u3f67\u470d\u3858\ua75c\uc0e8\u6a54\u4b74\uf068\ud490\uecfb\udebf\u78ec\u3210\u7d79\u97b2\u31fa", 795183505, 0x5BC794E1 ^ CryptoVault.mask()));
        this.manager.setVisibility(remoteConfig, bl, string -> {
            remoteConfig.isPublic = bl;
            this.backend.setStatus((String)string);
            this.refresh();
        }, string -> this.backend.setStatus((String)string));
    }

    private void delete(RemoteConfig remoteConfig) {
        this.armedDeleteId = null;
        this.backend.setStatus(ConfigsView.nw("\ud403\ud924\u4080\ue7c8\u5d8c\u3d2e\u1206\u0cc1\u64c7\uaba8\u946d", -1831182199, 0x5BC794E1 ^ CryptoVault.mask()) + remoteConfig.name + ConfigsView.nw("\u2767\u206b\u2393\uf90d\u9bca\u928c\ubb62", -1223523600, 0x5BC794E1 ^ CryptoVault.mask()));
        this.manager.deleteConfig(remoteConfig, string -> {
            this.selected = null;
            this.backend.setStatus((String)string);
            this.refresh();
        }, string -> this.backend.setStatus((String)string));
    }

    private void share(String string2) {
        this.backend.setStatus(ConfigsView.nw("\ufaed\u8c75\u4ea0\u79ae\uc591\u433e\ua697\u8e14\u6c5e\u44ce\ubd95", 514395711, 0x5BC794E1 ^ CryptoVault.mask()) + string2 + ConfigsView.nw("\u0d58\u06c1\uf520\u6580\uafec", -1878010905, 0x5BC794E1 ^ CryptoVault.mask()));
        this.manager.uploadConfig(string2, string2, this.shareDescription.value().trim(), string -> {
            this.backend.setStatus((String)string);
            this.shareDescription.setValue("");
            this.refresh();
        }, string -> this.backend.setStatus(ConfigsView.nw("\u4a05\u8e48\ucba6\u1e7e\u7f9c\u7593\ub6fa\ua99c\uf52e\u7508\uf5e2\ud061\u0c55\ua8bb\u60f1\u2003", 1058402818, 0x5BC794E1 ^ CryptoVault.mask()) + string));
    }

    private /* synthetic */ void lambda$drawCommunity$1(String string) {
        if (!this.filterTags.remove(string)) {
            this.filterTags.add(string);
        }
    }

    private /* synthetic */ void lambda$drawCommunity$0(int n2) {
        if (n2 == this.sortIndex) {
            return;
        }
        this.sortIndex = n2;
        this.refresh(this.lastUsernameSearch);
    }

    private static /* synthetic */ String nw(String string, int n2, int n3) {
        int n4;
        int n5;
        char[] cArray = string.toCharArray();
        if (cArray.length == 0) {
            return "";
        }
        long l2 = (long)n3 << 32 | (long)n2 & 0xFFFFFFFFL;
        l2 ^= 0x9E3779B97F4A7C15L;
        l2 = (l2 ^ l2 >>> 30) * -4658895280553007687L;
        l2 = (l2 ^ l2 >>> 27) * -7723592293110705685L;
        l2 ^= l2 >>> 31;
        long l3 = l2 ^ 0x5A5A5A5A5A5A5A5AL;
        l3 = (l3 ^ l3 >>> 30) * -4658895280553007687L;
        l3 = (l3 ^ l3 >>> 27) * -7723592293110705685L;
        l3 ^= l3 >>> 31;
        int n6 = (int)(l3 >>> 16) & 0xFFFF;
        long l4 = l2 ^ 0xA5A5A5A5A5A5A5A5L;
        l4 = (l4 ^ l4 >>> 30) * -4658895280553007687L;
        l4 = (l4 ^ l4 >>> 27) * -7723592293110705685L;
        if ((n5 = (int)((l4 ^= l4 >>> 31) & 0xFFFFL) | 1) == 1) {
            n5 = 19997;
        }
        int n7 = n5;
        for (int i2 = 0; i2 < 4; ++i2) {
            n7 *= 2 - n5 * n7;
        }
        long l5 = (l2 ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
        long l6 = (l2 ^ 0x3C6EF372FE94F82BL ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
        l6 = (l6 ^ l6 >>> 30) * -4658895280553007687L;
        l6 = (l6 ^ l6 >>> 27) * -7723592293110705685L;
        int n8 = (n7 &= 0xFFFF) * (cArray[0] - n6 & 0xFFFF) & 0xFFFF;
        if ((n4 = n8 ^ (int)((l6 ^= l6 >>> 31) >>> 48) & 0xFFFF) < 0 || n4 > cArray.length - 1) {
            return string;
        }
        char[] cArray2 = new char[n4];
        int n9 = 0;
        for (int i3 = 0; i3 < cArray.length - 1; ++i3) {
            int n10 = n7 * (cArray[i3 + 1] - n6 & 0xFFFF) & 0xFFFF;
            long l7 = (long)n9 * -7046029254386353131L;
            long l8 = l5 ^ l7;
            l8 = (l8 ^ l8 >>> 30) * -4658895280553007687L;
            l8 = (l8 ^ l8 >>> 27) * -7723592293110705685L;
            l8 ^= l8 >>> 31;
            if (i3 < n4) {
                cArray2[i3] = (char)(n10 ^ (int)(l8 >>> 48) & 0xFFFF);
            }
            l5 = l5 * 6364136223846793005L + 1442695040888963407L;
            n9 = n10;
        }
        return new String(cArray2);
    }

    @Environment(value=EnvType.CLIENT)
    private static enum Tab {
        MINE,
        COMMUNITY;


        private static /* synthetic */ String qq(String string, int n2, int n3) {
            int n4;
            int n5;
            char[] cArray = string.toCharArray();
            if (cArray.length == 0) {
                return "";
            }
            long l2 = (long)n3 << 32 | (long)n2 & 0xFFFFFFFFL;
            l2 ^= 0x9E3779B97F4A7C15L;
            l2 = (l2 ^ l2 >>> 30) * -4658895280553007687L;
            l2 = (l2 ^ l2 >>> 27) * -7723592293110705685L;
            l2 ^= l2 >>> 31;
            long l3 = l2 ^ 0x5A5A5A5A5A5A5A5AL;
            l3 = (l3 ^ l3 >>> 30) * -4658895280553007687L;
            l3 = (l3 ^ l3 >>> 27) * -7723592293110705685L;
            l3 ^= l3 >>> 31;
            int n6 = (int)(l3 >>> 16) & 0xFFFF;
            long l4 = l2 ^ 0xA5A5A5A5A5A5A5A5L;
            l4 = (l4 ^ l4 >>> 30) * -4658895280553007687L;
            l4 = (l4 ^ l4 >>> 27) * -7723592293110705685L;
            if ((n5 = (int)((l4 ^= l4 >>> 31) & 0xFFFFL) | 1) == 1) {
                n5 = 19997;
            }
            int n7 = n5;
            for (int i2 = 0; i2 < 4; ++i2) {
                n7 *= 2 - n5 * n7;
            }
            long l5 = (l2 ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
            long l6 = (l2 ^ 0x3C6EF372FE94F82BL ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
            l6 = (l6 ^ l6 >>> 30) * -4658895280553007687L;
            l6 = (l6 ^ l6 >>> 27) * -7723592293110705685L;
            int n8 = (n7 &= 0xFFFF) * (cArray[0] - n6 & 0xFFFF) & 0xFFFF;
            if ((n4 = n8 ^ (int)((l6 ^= l6 >>> 31) >>> 48) & 0xFFFF) < 0 || n4 > cArray.length - 1) {
                return string;
            }
            char[] cArray2 = new char[n4];
            int n9 = 0;
            for (int i3 = 0; i3 < cArray.length - 1; ++i3) {
                int n10 = n7 * (cArray[i3 + 1] - n6 & 0xFFFF) & 0xFFFF;
                long l7 = (long)n9 * -7046029254386353131L;
                long l8 = l5 ^ l7;
                l8 = (l8 ^ l8 >>> 30) * -4658895280553007687L;
                l8 = (l8 ^ l8 >>> 27) * -7723592293110705685L;
                l8 ^= l8 >>> 31;
                if (i3 < n4) {
                    cArray2[i3] = (char)(n10 ^ (int)(l8 >>> 48) & 0xFFFF);
                }
                l5 = l5 * 6364136223846793005L + 1442695040888963407L;
                n9 = n10;
            }
            return new String(cArray2);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static interface Backend {
        public List<String> savedConfigs();

        public Info info(String var1);

        public boolean signedIn();

        public void load(String var1);

        public void save(String var1, boolean var2, List<String> var3);

        public boolean canDeleteLocal(String var1);

        public void deleteLocal(String var1);

        public void deleteCloud(String var1);

        public void setPublic(String var1, boolean var2);

        public void rename(String var1, String var2);

        public void setTags(String var1, List<String> var2);

        public int cloudLimit();

        public int cloudCount();

        public String status();

        public void setStatus(String var1);

        public void closeMenu();

        default public String loadedConfig() {
            return null;
        }

        default public boolean unsavedChanges() {
            return false;
        }

        default public void overwrite() {
        }

        default public boolean premium() {
            return false;
        }

        default public void saveGenerated(String string, String string2, String string3, List<String> list, boolean bl, Consumer<Boolean> consumer) {
            this.setStatus(Backend.ql("\uc874\u834d\u3c68\uca48\u645c\u3c5b\u445e\u16ad\u660f\uafec\uf2d7\ua655\ud258\u9fe7\u68a2\u22c7\ubeb1\ue949\uaecd\uef17\u46f7\ud587\u0990\u34f0\u4d44\u8204\ue3b9\ub3e4\u164b\uc4ab\ub212\u6cee\uea88\u5f1e\ucff5\uc73d\ucafb", -1345944073, 0x46A42AEF ^ CryptoVault.mask()));
            consumer.accept(false);
        }

        private static /* synthetic */ String ql(String string, int n2, int n3) {
            int n4;
            long l2;
            int n5;
            int n6;
            char[] cArray = string.toCharArray();
            if (cArray.length == 0) {
                return "";
            }
            long l3 = (long)n3 << 32 | (long)n2 & 0xFFFFFFFFL;
            l3 ^= 0x9E3779B97F4A7C15L;
            l3 = (l3 ^ l3 >>> 30) * -4658895280553007687L;
            l3 = (l3 ^ l3 >>> 27) * -7723592293110705685L;
            l3 ^= l3 >>> 31;
            long l4 = l3 ^ 0xA5A5A5A5A5A5A5A5L;
            l4 = (l4 ^ l4 >>> 30) * -4658895280553007687L;
            l4 = (l4 ^ l4 >>> 27) * -7723592293110705685L;
            if ((n6 = (int)((l4 ^= l4 >>> 31) & 0xFFFFL) | 1) == 1) {
                n6 = 19997;
            }
            long l5 = l3 ^ 0x5A5A5A5A5A5A5A5AL;
            l5 = (l5 ^ l5 >>> 30) * -4658895280553007687L;
            l5 = (l5 ^ l5 >>> 27) * -7723592293110705685L;
            l5 ^= l5 >>> 31;
            int n7 = (int)(l5 >>> 16) & 0xFFFF;
            int n8 = 1;
            for (n5 = 0; n5 < 5; ++n5) {
                n8 *= 2 - n6 * n8;
            }
            n5 = n8 & 0xFFFF;
            long l6 = (l3 ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
            long l7 = l2 = (l3 ^ 0x3C6EF372FE94F82BL ^ 0xC3C3C3C3C3C3C3C3L) * 6364136223846793005L + 1442695040888963407L;
            l7 = (l7 ^ l7 >>> 30) * -4658895280553007687L;
            l7 = (l7 ^ l7 >>> 27) * -7723592293110705685L;
            int n9 = n5 * (cArray[0] - n7 & 0xFFFF) & 0xFFFF;
            if ((n4 = n9 ^ (int)((l7 ^= l7 >>> 31) >>> 48) & 0xFFFF) < 0 || n4 > cArray.length - 1) {
                return string;
            }
            char[] cArray2 = new char[n4];
            int n10 = 0;
            for (int i2 = 0; i2 < cArray.length - 1; ++i2) {
                int n11 = n5 * (cArray[i2 + 1] - n7 & 0xFFFF) & 0xFFFF;
                long l8 = l6 ^ (long)n10 * -7046029254386353131L;
                l8 = (l8 ^ l8 >>> 30) * -4658895280553007687L;
                l8 = (l8 ^ l8 >>> 27) * -7723592293110705685L;
                l8 ^= l8 >>> 31;
                int n12 = n11 ^ (int)(l8 >>> 48) & 0xFFFF;
                l6 = l6 * 6364136223846793005L + 1442695040888963407L;
                if (i2 < n4) {
                    cArray2[i2] = (char)n12;
                }
                n10 = n11;
            }
            return new String(cArray2);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public record Info(String id, String gameMode, boolean active, boolean isPublic, List<String> tags) {
    }
}

