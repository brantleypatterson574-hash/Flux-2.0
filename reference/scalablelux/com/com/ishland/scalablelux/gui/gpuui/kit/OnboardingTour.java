/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package com.ishland.scalablelux.gui.gpuui.kit;

import com.ishland.scalablelux.config.ClientPrefs;
import com.ishland.scalablelux.config.CloudConfigSync;
import com.ishland.scalablelux.gui.gpuui.framework.UiDisplayList;
import com.ishland.scalablelux.gui.gpuui.framework.UiDrawCommand;
import com.ishland.scalablelux.gui.gpuui.kit.SetupPicker;
import com.ishland.scalablelux.gui.gpuui.kit.UiHost;
import com.ishland.scalablelux.gui.gpuui.kit.UiKit;
import com.ishland.scalablelux.gui.gpuui.render.GpuUiFontMetrics;
import com.ishland.scalablelux.util.CryptoVault;
import com.ishland.scalablelux.util.PremiumGate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import me.sootysplash.box.render.HitboxConfigScreen752809;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class OnboardingTour {
    private static final List<Step> STEPS = List.of(new Step(OnboardingTour.xb("\u908d\u7f8b\ua4d4\u1b80\u724e\u0dcc\u0535\ubc8c\u7d25\u6eb0\u2bc3\uabd1\u966d\uae74\u2ddf\ube88\u2fc2", 1148052650, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ua6af\ua389\udcdf\u15c3\u8f2e\u6cb9\u9cfa\u7504\u0b7d\u6c32\u5389\u737d\ub9c0\u1af5\u7b57\u8a52\u6d49\u4585\u130d\ua7e8\ub2d6\u90dc\ued1a\ubf9b\ua8c6\ue7d6\u016d\uaac9\u9905\uc82f\u5c2b\u5649\ufa1f\u59dd\ua4c6\u8534\uec97\u7e83\u5e33\ua26f\uc73d\u5ad2\u4054\u50cf\u6809\u85fa\uba36\u124c\u4047\u9070\u8cc3\ubbe9\u0174\u824f\ue0ff\uf473\u4c2a\u9e26\u1e47\u0bf5\u4754\u80fc\u99a2\u465b\ub2da\u5bcd\u4f14\uf2da\uf2cc\uec63\ub61e\u0c2b\u879b\u1bb7\u57cd\u129a\u27f9\u1223\u38e8\u1591\u115d\uad29\ub169\u50e5\ue8a4\uaa5a\uaa2c\u1553\ucc55\u23e6\u4a74\ua057\u485f\uc303\u689c\u2202\u10bc\uef75\ucdae\ucd3b\u6a9e\ue1d6\u23e0\ueb40\u7a39\u1f3e\ue59f\ue2cb\uc208\u39f3\u0360\u9f7c\ufc41\u3367", 2047325988, 0x16AA02A4 ^ CryptoVault.mask()), Target.NONE), new Step(OnboardingTour.xb("\uade4\u354e\u9dea\u8f94\u1f16\u3f97\u1ddf\u09b9\u4ead\u6981\u83b1\ue20c\u301e\u766c", 800289086, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\uf35f\ua911\ued59\u9ed9\u9723\uaf85\u6b42\u849f\u2a20\udb17\u5461\u0094\u40a1\u8ad4\ub473\ufa94\ubff5\uac22\u20d2\u6e7a\u890c\u64ef\u6891\uf5f9\u0eb5\u0660\ubd7e\u44d1\u6365\u2c5c\u6899\u3350\u82ca\u1dd0\ub390\u16bf\u1aae\u56c0\uc75f\ucd1d\u3016\ucc38\u3a3e\u0377\u6876\u9b04\u2284\u91b3\u687c\u3916\u3a9b\u455c\u470b\u0761\u4d52\u0396\ufdde\u389b\ua8bc\u57bb\uddc6\ud9fe\u86ef\u6289\uecb2\u0bac\u1a63\u0b01\u9be5\uece4\u8061\u80e4\ufb15\u0f0a\uc2f0\u30a5\u6d4a\uef50\u6963\ub144\uaf2b\udf8b\uac53\u94d3\u026a\u161d\u81f8\u58db\ubc4c\uf4b3\u13e9\ua179\u6ed7\u22b7\u700e\ub346\u6eb0\ufb6c\u6a55\u13c2\u4fd7\u6f8e\uf642\u09ea\u6c1b\u2e63\ucbcf\u657f\uf56d\u4694\uc008\u10e5\uebaa\u955a\u8050\u36ad\u723b\ue7fa\u3c51\u4f76\uad4d\u8599\uf94d\u0662\u0b9d\u1997\ufa07\uc6c0\u6c69\uc1c7", 120202351, 0x16AA02A4 ^ CryptoVault.mask()), Target.RAIL), new Step(OnboardingTour.xb("\ub832\ue9ec\ufe97\uf679\ueba3\ufdfb\u5630\u7db5\u51e6\u2541\ucc0f\u8872\u47b1\u47ec\u3f43\ufe4b\u8bab\u234b\uc96a\u5285\ufced", -2126818735, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u7e5d\u2321\ua6ec\uca68\u330a\u845b\u75c8\u1470\ud15a\u4ac3\uc62c\uabcc\ubebe\uaeb1\u213f\u5049\u3d64\u059d\u201e\ub1f4\u5b67\u5836\u804a\u94f8\u285d\ua04b\uad74\ud4f0\ub506\ucc13\u8b26\u6d72\ubd85\u3163\ud9e5\u1906\u5177\ued59\u6d78\u3d0c\u00db\ub991\u7add\uf1ab\ue458\u5c61\uf908\uf92d\ud4de\u6efa\ua063\u94d2\ua0f5\u70dd\ubc0e\ua4c7\u0513\u44d9\u77c5\u0d54\uf837\u18af\u387e\ue844\uddd3\u60cb\uca20\u452a\u112b\u2166\u662c\uf69f\uf992\uea6c\u25cb\u805b\u2e0b\u4655\uf844\ue1a8\u056c\ufcca\u3fdc\uc532\u7a90\u6cbb\ud887\u0974\ud0d5\u5bd0\u0a9d\ubfba\u2aeb\u2660\u3580\ub13e\u4f77\u26e8\ufbdd\u36b1\u3a4c\uab8a\ua0d8\ua228\ubaf0\u656c\uacdf\u17e0\ueaac\u7d81\u204b\u4180\ud3b3\u2509\uf93d\u640f\u39c8\uce02\u09ac\u77be\ua4bd\u4797\u30e5\uf59b\u4824\uc4e7\ue000\u3046\uf3cb\u3be5\uf8c4\uea52\u4545\u881c\u4e2f\ue4e9\u3315\ua0bf\uebee\u1882\u3306\u695c\u39ff\u9cb0\ue452\ue71e\u4d0e\u5362n\uaba3\uf17e\ucf25\uaf86\u2dd5\u55a1\u8865\u0cce\u80bd\ub0e0\uc11a\ube78\u6ce4\u7b31\ue762\ucd3e\u7f78\uf8f0\u0707\u3baf\ub644\u3e32\u4d41\u2056\u4b82\ubd5e\u5e5e\u3ba8\u76f8\u1680\u36a5\u9241\ud8b9\u0884\ucbf1\u6fb7\u20fc\u2df0\uc4d7\u6153\ud880\u9672\ued71\ue188\uf932\ub12a\u25f0\u139a", 735889176, 0x16AA02A4 ^ CryptoVault.mask()), Target.CATALOG), new Step(OnboardingTour.xb("\u5ce3\uaadc\u1b7d\u44d8\uc157\u1f6d\u1b23\uf6dc\uf98c", -514045012, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ue47c\u9089\u4e46\u8004\u3bc8\uaa16\u23ab\uf335\uc595\ub794\ucd44\uc220\u8e1c\u8fbe\u28df\u8d5c\u3feb\u835d\u9fb9\u6504\uee80\u8c24\u8e72\uca40\u8978\u872f\u5ff4\u60d7\u8a11\u3680\ud0e9\u44d6\u3798\u45eb\u6c51\uf5b4\uc3fb\uc1a8\u74d1\ud588\u2cd6\udfd7\ufb9c\udd7e\u9969\ua967\u18ab\u9fdc\u1d21\ud36f\u5f6d\u2770\ufdd3\uf9ce\u7eda\ue23d\udd6a\u6172\u9d14\u2c10\u60a6\uc8f3\ubd26\u236e\uaedc\uc524\u6761\ubd9e\ufec9\u830a\u1a27\u5255\u3926\u1293\ucf05\u54b1\u7b17\u1817\ufb2f\uaff8\u4e93\u249b\u89d4\ucbc6\u0962\u5c3c\u8ce6\ucbeb\u3ae2\u8e7e\u30ac\ufbc6\ud32c\u1d1f\u6ff3\ub1bd\u0585", -233173302, 0x16AA02A4 ^ CryptoVault.mask()), Target.CATALOG), new Step(OnboardingTour.xb("\ueb00\u07dd\uc51a\ue022\ude33\u26ec\u271b\u23a2\uffb7\u4afa\u0238\uf204\u689f\ua3d8\u2a80\udf69\u40a1\u840d", -1913279627, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u1537\u1a6b\u3037\ude01\ubcf3\u0c72\uf376\u6846\udc89\ud682\ua346\u5192\u4976\ud047\u53fd\u3d37\uaad7\ud202\u94c3\u8848\u8199\u4eed\u7f7d\u7e35\uddfd\u1d64\u76f1\ub617\u8035\u425e\u6e76\uc332\u3358\ud659\uc572\u3c32\u5a9b\u5c63\u2d2d\u39e3\u297a\udbd4\u2d0b\u8b35\u2c35\ua7eb\ue33c\ubf25\ua474\ud23b\u493a\u009f\u9d9c\u5e22\u7830\uc9a5\u0fa5\uf687\u097f\u7046\u6335\ue965\uab95\u8555\u4bb0\uf44f\u9b1d\u39a5\u6a8b\u679a\u0886\u99ec\ue38b\u0436\u891b\u7bf9\uc89a\u06ae\ud961\u70d7\u785d\ufafe\uc7df\u03e7\uf798\u4ead\u7b76\ucd36\u48e9\u4c75\u133e\u6463\u820e\u1eb0\u9026\ua7cd\uea75\ua443\u17b1\uad77\u17ad\ub637\u29d9\uf0bf\u6a6a\ud339\uad54\uf0b6\ud41b\u61af\u7068\u1e1f\uae89\uc1c3\ua021\u1d6e\ue5d5\udac7\u2ccb\u74d8\ucf78\ue087\ud55b\u50a0\u2e1c", 496823678, 0x16AA02A4 ^ CryptoVault.mask()), Target.CATALOG), new Step(OnboardingTour.xb("\u9e8c\u044a\uffaa\u953b\ubf77\ub9f3\ufaf1\u7d74\u3cc5\uc8e9\u8578\ueadf\ueca2\u3715\u9dbd\u5640", -369384466, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u00c7\u352c\u9aca\u169e\u6edd\ub777\u738b\uee52\u8de5\uc921\u7bd5\u09ce\u6ce7\ue238\u766f\u5456\u08a6\u8dd3\u7d5a\ufd74\u27d6\u78e8\uecf4\u93b8\u906b\ueb4b\ua047\u92e7\uafa3\ua26a\u3870\u2693\u3e36\ucf28\u68f5\ud133\uef5d\uf268\u7bb8\u5209\ub5d3\u0763\u2f51\ufee5\u567f\u4ba6\ube5b\u0351\u0643\u750f\u667f\u673c\u7ebe\u082a\udccb\uc742\u23b7\ub5b1\u7f90\u2e86\u88de\uee10\ue941\ub2a5\ud297\ua236\u018f\ubb3a\uc6f9\ud918\u1f83\uebea\u99f4\ubf91\u35db\u1380\ueea3\uebb5\uf502\ud90a\u7cf5\u60de\u4ab4\ud8af\ue4ed\u055b\ud627\u5177\uf4c1\uaa8b\uc80f\u308c\u7504\u0bb5\u21f4\u8dc5\u988a\u5da8\ub2b6\ub989\ua63c\u9102\u5de1\u4936\ue3bb\ub0c2\ud6fb\u9be2\uaa7d\u1287\u9cc8\ufad8\u8bd0\uc36c\uac8c\u4672\ude4b\u1e10\u85e2\u4877\u9aaf\uc932\ucaa7\u7a08\u105f\ud3d6\uace4\ua5a5\u56ee\ud8ce\u644d\u5494\ue567\u0225\u0ae8\u14a5\ua94c\u22be\u1e48\u6d9d", 396723599, 0x16AA02A4 ^ CryptoVault.mask()), Target.CATALOG), new Step(OnboardingTour.xb("\u5a04\uae85\u91a3\u80d8\uf75f\u3e3e\u7a0e\ufd0c\uff25\ub1f4", -1548274014, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u1d4f\u0b91\u34ba\u98ba\ub7e3\u4a54\u2318\u984e\u64f8\u034e\ub71e\ub132\u076b\ud2a0\u94d6\uf3c5\ue851\ud895\u032d\u3ef3\u5cc9\u6cd8\u4d77\u7304\u5cff\u16b3\ud6c2\ue1b5\u3cb7\u3c64\ue2e5\u9969\u4031\u4d8d\u6e61\u3296\u1095\uecb5\u6746\ue114\u5624\ud551\u85c1\u1f75\u2025\u583a\u8e1c\u06fb\uc8f8\ue4e5\u9d36\u4089\ubcb1\ud226\u4d25\ucfe4\u1a59\u5c0b\u872e\udd23\u36e2\u23be\ub838\u789a\u2ea2\uf045\u0d12\uca20\uad8c\u2122\u0b83\u6af3\u04e4\u154a\u7749\u9b53\u1583\ud064\u8156\u8cd6\ua6e0\u90c8\u9039\ufec8\u947a\u4b34\ua55d\u6831\u8344\u8540\u7f41\u6f82\u71c1\u8e36\ud691\u8596\u7ba2\u578d\ub387\u9365\ub9c1\u893d\u1b22\u394e\u3b8f\u1094\u133d\u8e92\uec01\u8108\uc8a4\uc1ca\ud8c3\uaa2a\u2d05\u0618\u9818\ub1fd\ud8c8\u9373\u10fb\u68ff\ud156\u5c00\uf020\uffc5\u1893\ue5e9\ub7c0\u76e6\u7049\u3fb3\u3b4a\u5d45\u37d6\uddd1\u7436\u9a43\u6188\u7d16\ua77d\u0d0f\u62ab\u26cc\ubbd5\ucf3d\u5756\ubf80\u6a7d\uc9fb\ue8a8\ua98a\ub413\ue84f\u90f0\u99aa\u62c5\ua738\u0f20\u5c85\ude60\ube4c\ubc52\uf556", 861119831, 0x16AA02A4 ^ CryptoVault.mask()), Target.RAIL), new Step(OnboardingTour.xb("\u8f9f\u1035\uc1f0\u392d\u86a9\u38d9\u37d0\uc86a", 1526314680, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ua3ff\udadb\u613b\u761b\ua725\u586b\u06b0\u010d\ua433\u847f\u8310\u6204\u01f2\u34c1\u32c5\ua48b\ub4ff\u4d63\u91f3\u1954\ub69f\ub702\uab92\u370b\u83b4\ucb60\ua809\u0b12\u4f77\u3aba\ua937\u930c\u3d7b\u5eaf\ueb80\u4c81\u98ef\u3204\u6be4\u37fa\uf6d5\u6149\u20bf\ufe20\ud9cb\u8d82\uc574\u08a0\ua324\u290b\uff6e\ua1f5\ub76a\ua50d\ufa23\ufee4\ucb3e\ucbac\u537c\uafdd\u32bf\u6c3e\ueaf2\u0571\u279c\u95c3\ua842\u94a8\ub922\u9183\u1d96\u80f5\ufdb4\u1ab3\uf551\ua9cd\u5584\uf9ba\u4bab\ucadb\u306d\u9ef0\u3a67\uec95\u98c2\u4f37\u331c\ue058\ud4f9\ubee6\u5fd3\ue812\uf61a\u4a9e\ud06f\ufce0\u5dff\ua20f\u6ea2\u4625\ueb61\u51f7\u138b\udb89\u616c\udac5\uace5\u0256\u0932\u0913\u3b36\u5bcf\u966d\u7b8f\u3b72\u6f04\u857d\u9a4d\u9453\u61a3\uc1c4\uc072\u031e\u56b6\u1e95\ubaf0\ud140\uf9ba\u31f5\u9e91\u47e4\u5720\ub0bf\uc25c\u2455\u2922\ucfc7\ua31e\ue6a2\u246c\uaf18\u9e33\u32f1\ue573\u1663\u1b77\u3b3f\u820a\ua76d\u8343\uda73\u366c\ua439\uef3b\u9ec8\u1094\u7e30\ub466\u1fbb\ud668\u23aa\udff9\ue51b\u891f\ua5d5", 492178264, 0x16AA02A4 ^ CryptoVault.mask()), Target.CATALOG), new Step(OnboardingTour.xb("\ucf55\ucc72\u9b5a\u09c0\u9774\u126e\u372d\ub06e\ua102\uaeac", -984327607, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ud5c0\uc421\udd99\u0507\u89ac\u00c5\uc155\u7b08\u1a0a\u518d\uecaa\u8832\uc74c\ub29a\ud23e\u28c3\ub631\uc975\uf738\u54cc\u34a1\u4461\u1f7e\u622a\u6616\u443d\u8a85\ua726\u70ae\ucb10\u327f\u1740\ua536\u2476\u2271\u8a20\u5a60\uc334\u0cdd\ua39a\u9640\ucb68\u8ae0\ub8c0\ue450\uefee\ue780\ub9f6\ua4ea\u30b3\u9198\ua1a4\ud726\u5bd3\u1f3b\uc95d\uf32e\u3091\uc4d5\u053e\ueca6\u2438\ua4db\u3684\ubb45\u2fb1\uc24e\uc337\ud251\u3da0\u1b87\u7fbf\u9930\u0148\u1365\udf1b\u5b7b\uc9ea\u045e\u373b\u15f3\u8fb2\u4ce5\u9edd\uceb8\u2ea8\uccbf\u2a6b\u0916\u2815\ue2b2\u51d8\u1b42\ub3a0\ue27c\u6dbe\ubaaf\ud2c6\u73bb\u871e\u9705\u1dce\u5bd1\u47ae\u963c\u29ff\ub536\u5281\u6841\ua469\u893f\ubd7e\u2f24\udaef\u8871\u8291\uac66", 1522393438, 0x16AA02A4 ^ CryptoVault.mask()), Target.CATALOG), new Step(OnboardingTour.xb("\u60ac\ue8cd\ua6a2\u3f63\ucad9\u3cda\u40c4\u9b6b\ud212\u343b", 68388765, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u2432\u1221\ud0e6\u6185\uedcb\ucead\u2953\u1193\u2ee7\u8d5d\ucaa3\uec49\u6fc8\ue7ff\u6a93\u0899\u814c\u7493\u2986\uc899\u5d74\u2b6b\uec4e\u4c8e\u4c1a\u586d\u6a93\u350e\uf610\u1453\u21ca\u96e8\u6362\u080c\uf8eb\ue80e\u0eed\ub25d\uc0c0\u8531\ud8f4\u400b\u39d2\u6c18\ue0a7\u8aaa\u1973\u3944\u44fd\ud189\u8a6b\u4510\u9093\u3dfb\u1c56\u1f30\ue2c3\u0d37\u500b\u3c97\u2964\u888b\u5559\u1d0a\u29e1\u97b0\ueba5\uf9f8\ue38f\ue8f6\u7f83\u458c\udad3\udf71\u6f2e\u4f9b\ucb10\u20d5\u5e4d\u72d4\u5363\ud6c2\u70ab\u69a1\uf6b4\ua19e\ue701\ud49b\u9e4a\u7e40\u718b\u527e\u3343\ud0c3\u2d8f\u4293\u4156\ua2c8\ub1d6\u812c\u4c34\uea7f\u8086\u36ac\u938c\u5278\u187c\u826e\u641e\u214d\u8f2f\u183b\u3a28\ue023\uc8c3\u8d9c\u6791\ud4c3\u5944\ub6f0\ubfcd\ua686\u69b2\u0ae6\ub862\u97b6\u689a\u64d4", 653091409, 0x16AA02A4 ^ CryptoVault.mask()), Target.NAV_SETTINGS), new Step(OnboardingTour.xb("\u8602\u0015\u8b38\u948a\ub297\u16d3\u8aba\u624c\u9503", 1313314742, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u4c95\u69d0\u7527\u38c2\u1661\u66d3\u6248\u2fc6\u19e9\u5238\ua1de\u986a\u548b\u6fa5\u58a5\ubedd\u019e\u16c8\u0944\ua004\uff4d\ub4a9\uf698\u0a2d\u3d70\ub6dd\ua666\u3fae\u3c31\u60ec\u5cff\u5235\u1996\u6e24\u5509\ue184\uc637\ua562\ub1c6\u5356\u26dd\u8816\ub189\u7461\u7f3d\ub3c0\u1eec\u4637\u3aff\u7b7c\u1fa5\u0342\ub4c8\u22fc\udb14\uafcc\u2651\ub6e1\ubf94\u9be9\u6d9f\u3417\u526c\u0b01\uc3b3\ud7e7\u79bd\u19fc\ua8e8\uceb8\u404b\u1c36\u23a4\u44f7\u98fb\ue0ec\ue13e\u72c0\ue487\u0f32\ucfd9\uf950\u8bf0\u5551\uf626\u6526\uc0cb\uc9d9\u557c\u098c\u893c\u19de\u138b\uc7f5\uabdd\ueb5d\u1dee\ua13e\u83ff\ua2ee\u5cc6\uba16\ue2d2\u3e08\uc342\ude2d\ub765\u0aa8\ub3ab\u1bf5\ue179\u6a42\uc0be\u9183\u4c28\u8710\u45f0\ue6d8\u1eec\ua11e\u36fd\u2549\ubbca\ud68e\u64b9\ue3e6\u17d9\u7de1", 588012542, 0x16AA02A4 ^ CryptoVault.mask()), Target.NAV_CONFIGS), new Step(OnboardingTour.xb("\u2e11\ue872\u9722\u2c8d\uad15\uae82\ub025\u1127\uc4f0\u17e7", 894062745, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ud433\u1ab8\ue0ce\u83f7\ucd0c\u2883\u4850\ua97a\u565c\ua887\u5dec\u6f5b\u367a\udbc0\u63b7\u5731\u14bf\ub1f4\u8746\u8d1d\u7e09\ue3ee\ub5a9\uffb0\u1a2e\u7db8\u1566\uc8ef\uf237\ub62f\u6daa\u1ef9\ub0e5\u92c2\u5b73\u79a6\u97f1\u996c\uba92\u433e\u693b\ua0d5\u81b8\uc88f\uce0f\u8b75\ucd52\ue108\u9699\u91ad\u20d6\ub20b\u347f\ue042\ua132\u1627\u31be\u35a1\u984e\u0377\u62f4\u6fe2\u723c\ubec5\u8da0\u74ea\ud2df\u8594\u9f4a\ue07b\uee9f\u2199\u3cf9\u0a92\u05d6\u2a8d\u30e3\uad7e\ucce7\ucd87\udcd1\u5f91\u5044\u1de0\ua5d5\u7b04\u783a\uf1f9\u1685\u1c66\uff9d\ua023\u9c99\u9daa\ue002\u2b96\u481b\u70db\u25aa\uf83e", 1655412669, 0x16AA02A4 ^ CryptoVault.mask()), Target.NAV_PEOPLE), new Step(OnboardingTour.xb("\u23ce\uea0a\ub6b0\u99ad\ufaa4\u806b\u6eef\u4a2e\ub85f\u731f\u41c3\ucef8", 1258528693, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u4eaa\u4bdd\u4559\uf324\u542e\u0365\udb2a\u35ba\u787c\u6be5\uf181\u1c2d\u885b\u323b\u9e78\u3276\u7c00\u61e0\u4f08\u38ed\uee3d\u5ef6\uf98b\ua8fe\u11c5\u0d6e\ub44b\u7521\u32f0\u0932\u023e\uf724\u1e1e\ub885\ube43\u1810\u9bcf\u1930\uadc8\u67b7\ud558\u98ed\ud115\ue3af\uba3b\u8600\ue6d1\ue2b7\u78d7\u5f63\u0662\u2db5\u2740\u90a6\udce5\u184a\uf5ee\u989e\u4774\u9db1\ucbcd\ud1a8\u3269\u57a6\u0972\u72e2\u8f39\u7f5c\u5a59\u15dd", 1425376206, 0x16AA02A4 ^ CryptoVault.mask()), Target.NAV_HUD), new Step(OnboardingTour.xb("\u8024\u68d6\ua49d\ud3d6\u65b1\u823d\ue42b\uc604\uc765\u1db0\ud3d6\uca7e", 158373118, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ud077\u2345\u28f3\ud057\uc9b6\u174b\ubadb\u4f7c\u8a8b\u2895\u4d03\u4faa\u6087\u55b2\u9de3\u103a\u34a5\u16c4\u3cd1\ud7ba\u810c\u86f1\ue8f5\u3012\u3d7f\u7e0b\u9dd1\u8e64\u14f4\ube13\u722f\u26c7\u85de\uca8e\u8e27\ua6e0\ucb94\u75ac\ua7bc\u7c9f\ucdb4\u72d5\ue198\u03ec\uc4cf\u0f75\u7043\u527f\u3f25\u9660\u3b32\uf02b\uc291\udbfb\u80b9\udda4\u7283\ua262\ube45\u4b42\ua7bc\u89bd\uc462\ud8ac\ucee7\u5d5c\u4e6b\u2e03\ufa3f\u0d6b\u526d\u9527\u9623\ufb1a\u1846\u6c96\ubddb\u3a11\u5305\u3ba7\ua321\ue02f\u78f6\u6e63\uabc8\uf704\u855b\u1a90\u9a49\u53ce\u54f9\u7c4a\ubfe8\uc9fe\u861d\u56db\uc652\ue717\ub5d3\u4dce\ue695\u2dff\u8300\u15be\uc352\u51ff\u6315\ud496\u0cc1\ube75\u2bf7\u6549\u4e28\u124a\ue531\uc209\u3c22\u8967\uffc8\ub596\ue294\u6447\u2499\u97a7\u1420\u0f52\u8fbe\u638b\ue5c3\ub99b\uc358\u7d73\ua799\u3059\u15f7\u26af", -704131831, 0x16AA02A4 ^ CryptoVault.mask()), Target.NAV_HUD), new Step(OnboardingTour.xb("\u05e5\u0205\u3c48\ucf59\udefd\u7218\u226f\u3043\ub7d3\uec26", 641044796, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u5037\u72a2\u1498\u2df2\ue4d7\u7d3b\u1465\u3323\uf10c\udbdb\uc307\u894f\ufd04\u199d\u8f9f\u869f\u0d14\ubba2\ud090\u35b2\ubbf9\u2536\u28cd\ue173\u67cd\u6a30\u65f6\u0336\u0315\u48b7\u7834\u84de\u3968\ueffe\uec20\u00aa\u4daf\u5f2d\ubef2\u3927\u3203\u8fa3\u0a30\u535f\u64e2\u9829\u64ad\u9ccb\u7fb4\u020c\ubed7\u6e5c\u336e\u3ab0\u3a1e\u7562\u229f\u3451\ua4bd\ue604\u2902\ud3ae\u1369\u1d02\u84a7\u6c5b\u66c5\u1a0b\u37ed\ub972\uf2db\u33b8\uf8d5\u03bb\ue08f\u5652\u8bad\ud750\u673a\u83a2\uc961\u1ba2\u4244\u66e1\u8026\uae43\u024d\uab5f\u2e68\u9d75\u1b5b\ufe1e\u4aa0\u7ab3\u01e0\u38e9\u6b6e\ucbd3\ua4b1\u770a\u3942\ufab6\u9f63\uc9f9\u4f59\ua327\u95b9\u97eb\ubb3f\u75e3\uad1b\u60d5\u600c\u877a\uf64f\ud171\uc688\u6594\udc5b\u374e\u5587\u643d\u17d4\u873a\u035c\u67aa\u9c9a\u5d82\u9f2c", 321886754, 0x16AA02A4 ^ CryptoVault.mask()), Target.RAIL_MACROS), new Step(OnboardingTour.xb("\uf34e\u885b\u5553\uea69\uc66c\u66e5\u8703\u1bb7\u8db0\u90d5", -156147873, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u1ef2\u0e41\u7680\ua25b\u49a5\uff84\ud8ec\u668e\ua018\u37b0\u6e5c\ua7c4\ubc70\ue036\ueec7\ua0f5\uaa9b\u21b9\ue145\ucdee\u2559\u804f\u0b7a\u8fd8\uc735\u3982\uebf6\ue5cf\u3d94\u8ff8\u4399\u480e\u2637\u8ac5\u5782\u4dec\uf8ec\u4b44\u09e8\u12fd\u6ab0\uf38b\u2324\u9587\u069e\u6e74\u678f\u5628\u43ce\u5742\udf15\ue422\u4486\u0d7e\uefc7\u8590\ud900\uecc3\u945d\u198d\u0daf\ue588\u0854\ue255\u051b\u64ae\u26df\ud18f\u5848\u4824\u94fb\u0520\u23cd\u6499\u7317\u28d0\u5ef7\u6d21\u06e8\u8b9b\ud395\u4e72\ud07c\uc5cd\uc6d0\u3387\u15bb\ua202\u1b8b\u0ad1\u395d\u222b\u81b5\u05c1\u4310\u9fac\u6342\u7732\u91d3\uadb6\ucc07\ufa28\u6078\uc4b5\u0712\u0681\u1964\u51dc\u4137\u6e5c\u9313\u1be1\u8763\ucac3\ub74e\u7bce\u6724\u493e\u6516\u0d0a\uade6\u9dfe\udfbc\u2eff\u9029\uf1be\uf367\ude1a\u1891\u924a\u3fb9\u1dae\ub899\u1f10\ua5ec\uea05\u01f3\u6a8b\u3a85\u08d3\ube49\u1be6\ubc1e\ua16b\u3792\ua341\u6ade\u8ed7\u2862\u8200\u63ae\ub043\ube0d\u6494\ua94b\u9e11\ue164\uc9ea\ucdb7\uc2ca\ud2ab\udd91\uc098\u8871\u0842\u4da0\u3a5c\ua2b6\ubbce\u1cf2\u0391\uc3f5\ud0c2\u7eb2\u6c83\u911c\u386b\uf282\u50e1\u633f\u59bf\uf844\ue992\u9f2d\u6c54\u9865\u4ddc\u5c03\ue81d", 1109559011, 0x16AA02A4 ^ CryptoVault.mask()), Target.RAIL_SCRIPTS), new Step(OnboardingTour.xb("\ue06a\uec42\u4bff\ue4c7\uea9d\u667f\u9f1a\u3899\u3d3f\u8057\u5765\uf719\u4b35\u1fa1\u7e40\u051c", -1851404927, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u3ddb\u1222\u41cf\uda26\u7629\uf7cf\u805b\u907f\ua796\uf0f7\uc8c6\u8f26\u0fe8\u0a1b\u1128\ub938\u9590\ue776\uebd7\u3680\u54e6\ub88e\ue259\u05bf\u484b\uc2c2\ub7de\u85d1\u70da\ub128\u88f6\u3a03\u3ea8\ufa4a\u5389\ud1bb\uc1a2\ueac7\u15a9\u0995\ub740\u4633\u0636\u4260\u7184\ue5ea\u81dd\ud145\u2bd5\u93ac\u5baa\u7f65\u665a\ubd5f\uf294\u2ef0\ue2e8\u8f1f\uf6d5\u4844\udfa5\ufaf0\ua822\u0d46\u66d2\ubc6b\ub252\u6419\ue8ab\u3a97\u8235\u58f3\ucf46\u029d\udc4b\u4deb\ud59a\u7e0b\u6039\u239a\u01d9\u3980\u2ffd\u095b\udf6a\u8743\ua452\ube96\ua6cc\ub607\uab5a\u87ee\u2658\ub10d\u7e8b\uf398\ufffa\u488f\uf12e\ufc7b\uf474\u76e2\u9405\u8743\u47d4\u6cf5\u0f4f\uf7e2\u728e\u7757\u2379\u749d\u9ce9\ud4b5\u1d25\u6658\u3b48\u0aa7\u8230\ufe2b\u35ca\uf2fc\ucb1f\u5353\u968b\uf296\ub23d\ucd22\uffc2\u33c8\u8e9b", 648637005, 0x16AA02A4 ^ CryptoVault.mask()), Target.NAV_CONFIGS), new Step(OnboardingTour.xb("\ud375\ub15b\uf609\u446a\u0caa\u417d\u95d9\u2a69\ua654\u9529\u8fb0", -670435340, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u53a2\ua4e3\u4ba5\u458d\u633a\u5b9e\u6147\ua680\ud4d1\ua4b2\ua692\u9581\ue846\u135c\ud0d6\ua814\u4da9\u28be\u39ed\u510c\u2f1d\u58d9\u282a\ub7b2\u5b61\ue8de\u8e2a\u011f\ua9b1\u0a9e\u5ca6\u420e\ub921\u1120\u94f9\u503e\ubf8b\u4e11\ubada\u3e6d\u79cd\uce70\uff39\u760d\u3770\u0c18\uedfb\ud683\ud330\ud399\u7760\u0b1d\ud0a3\ua359\u3dba\u1bc1\u9732\ue770\ube65\u57f4\uc3d5\u3cd8\u1f52\ub0a9\u68db\u7267\u0bca\u7ade\u0fbb\uf4ed\ue3c0\u4eb4\u7524\u772f\u80ad\uca78\u4ab0\ua45e\u28ee\u4b2f\u9077\ud1d7\uf45d\u31e8\u597e\u6460\u5f87\uc7a8\ufe09\uf47c\ubc37\u2621\u4d8b\u166d\u53e2\u524e\uceab\u433a\u5528\u486a\ua603\ucf8e\u8ed7\ubddc\u086f\uf3b0\u84af\uf6a5\u3301\u8b2e\uc5b8\u765b\uc948\ub8a7\u8eb3\u4074\uc195\u95e6\u6830\u7983\u8515\u80d4\uf337\uf5fd\uabb6\u2e4b\u7e1f\u1b58\ufa39\ud3ae\ubd0a\u0d08\u4ee1\ucc3c\u70ed\u5cab\udaf2\u94b0\uc1d1\u8a8b\u2650\u1b82", -1695687717, 0x16AA02A4 ^ CryptoVault.mask()), Target.NAV_CONFIGS), new Step(OnboardingTour.xb("\u66e0\ub78d\u056d\u5d62\u1738\u990c\ubc2b\u3f20\u9ab4\uf722\u7a95\ua994\uec83\u32c1", -803857747, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u9f83\u796d\u917b\ud6ca\u8838\u8ae1\u8052\u4422\uf472\u30d3\u9f2e\u7d47\uab31\udfbe\uc15b\ubeb7\u9a52\ue776\u06ba\ua8ab\u21fe\ucef0\u7a73\u8a4a\u067f\uaa12\ua4c9\uc919\u2665\ub3ea\uda44\ufb27\u9258\u9076\u2b35\u421b\ub62c\ud395\ud48b\ue9f6\u806d\uac85\u9eb2\uf90e\u7e3f\uaa04\u5d99\u3f1b\uac27\uc524\u6089\uf5be\u85b7\ua285\uef40\u2c65\ua359\uab02+\u29c2\u57fa\u409a\uc54f\u893c\uf5fe\u0105\u20ff\uc4ad\u9788\ue69e\u1741\ueba0\u185e\ub195\ub212\u76bd\ub091\u5e25\ua4b4\u96f1\u1617\u3436\u04ba\uf489\u3a36\ub02a\u5cdf\ufc0e\uba36\u7f55\ue9c6\u67b5\u5cd8\ue6e4\ud8f8\ucf89\u421d\u96cc\u2762\u4756\ud9d6\ua747\ub166\u9d3d\ub7a7\u78b5\uf6ff\uf0bc\uc0b4\u2abe\u2b39\u5aee\u613c\u17ea\udd2b\ufc70\u8ad6\u8d1e\ub7d9\u2928\ud3e0\u3461\u88fa\u566c\uaf19\ud292\u8e79\u5683\u42eb\u67fe\uba2a\u3c37\u699e\ub517\u5800\u3e64\ucb8f\u6575\u9fd8\ube7f\u7476\u026f\u13a1\u427d\u0d46\u0905", 294932138, 0x16AA02A4 ^ CryptoVault.mask()), Target.NAV_SETTINGS), new Step(OnboardingTour.xb("\uf97b\ub5bf\ue7c0\ua1c4\uaf78\u5664\u2de0\u400d\u52fc\u3900\u73d7\uca1b\ue769\u6b15\u0184\uec86\ue953\u16b7\u08f9", 1413852468, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u6a5b\ufae4\u5a83\u3b25\u3a15\u44fd\u83f6\ud869\u7d6f\uadc6\u45bb\u3d50\u8124\uc9b7\u7cc2\u0677\u2f05\ub5ad\u82ed\u4669\u9880\u535f\u0185\ud05a\u3a07\u249c\u1b3e\udb2e\u756d\ue73c\uc94c\u4df7\u013e\ue782\u7cb4\u3191\u8081\ua96d\ucf9d\u22cc\ueebb\ud952\u0dd0\u5951\ubcdb\u3da4\u2c07\u4e0e\ub83e\u5408\u2c33\u9b06\ue965\u410c\u8fed\u7258\u1553\ue733\u8e68\u2227\ubc92\u6a2d\uba4a\u7281\ue39b\ud230\u9c3e\uf0ab\u5dfe\uacf2\ua355\u7749\u06bb\u18de\uc0eb\ud565\u6276\u5152\u754f\u19b7\u2116\u21ec\ud35d\u5b1e\ufe8b\uef29\ucdef\u64d1\u7fb4\u41bc\u8fae\u1135\uba67\u9f01\u39be\u5b08\uecaf\uff91\u0242\ue873\u684c\u5e6a\ue37d\u36ac\u1468\u3fd9\uc329\ubd5f\udc86\u9f58\u6101\u8412\ub953\u662b\ud816\u3fbc\u962b\u42ae\uff9e\u3a16\u15c7\u8a86\ucec4\u64f8\ubd3d\u9709\ue92e\u751c\u0f53", -224582696, 0x16AA02A4 ^ CryptoVault.mask()), Target.NAV_SETTINGS), new Step(OnboardingTour.xb("\ub0ac\uf62e\uad81\uf318\ud7a6\u1de6\ufc38\u4cbc\u99f0\u8df4\u461b\uc632\ud358\uf205\uc0d9", 1218778786, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ub014\uc3ee\ue27c\u2c30\u4e52\u2c66\u0769\u5b3c\u6c2c\ue65e\u0668\u4e54\u54ba\u81c3\u1102\u2b64\u4ff8\u16bb\u9cb6\u31ae\u9e06\u56db\u5de4\u66bd\u2d4f\u33a7\u34b4\ufd24\u3938\u2d2e\u5016\ucc18\uea25\u461f\u1519\u6a06\uf9ac\u5878\ub8bc\u8803\u4eac\u81b5\u4b00\u9690\ubf4b\u4914\u7345\uc56c\u1e6a\u8a42\u3908\u64cb\u8389\u4f58\u7bc7\u7232\u8b68\u3583\udb13\u0296\u4cf7\u909b\ud663\u19be\u898b\u5ce9\u3d27\u08cf\u24d6\u30ee\ue68d\u1968\u5095\ufefb\u85d1\ue397\u1c6b\u3ed6\ue273\u4d4c\ufa73\u598c\u99e1\u6b72\u884c\uf6ed\u6953\u2b5d\u8cff\u7f5c\u38cd\u6229\uc201\ua3a3\u0623\u9350\u99a4\ud20d\ue97a\uad75\u40ba\u16fb\u69dc\u1a57\u503b\u51c9\uea52\u86ad\u2e5d\ua6f2\u4e33\uc28b\u0a16\u5a66\ubf01\u92a6\ua99c\u5e76\uc59f\ue592\ua277\uafe5\ued66", -602858557, 0x16AA02A4 ^ CryptoVault.mask()), Target.NAV_SELF_DESTRUCT), new Step(OnboardingTour.xb("\u925d\u36d7\ua30a\u617f\uf0b0\uc6c7\u3a3d\u60e5\u8ebf\u6a35\u22aa\u6a7f\u29fd\u3e81", 999662816, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u2e81\uf9b7\u436d\u8338\u22f8\u5423\u99f7\ubfdf\u3c94\uda21\ufd78\ud1d7\u13d3\uc9e0\u73a8\u42f8\u785f\u47e4\u75d9\u3bf0\u72e2\u7463\u979a\ueffa\u3923\ufa1c\uf5a2\u984d\u1a54\u8832\u656e\u5ce3\u4ac7\u2fd3\ub70e\ucebf\u6970\u3271\u1fc1\ua940\ub4ca\uc99c\u873a\u5cb5\ue727\ud03e\uc40b\uda5a\u28dc\ud890\uce62\u5c61\uca44\u9bb6\u6d5d\u06d9\u6a11\u7907\ub51b\u7fb1\ub140\u44af\u82b6\u46c1\u8b0a\u00bf\u3ca2\u3423\u1a39\u1f4d\u9207\ua525\u9ad8\u4f57\u3472\uc764\u9b9c\ubf4f\u12aa\ub774\u51b4\u8e0a\ue6d7\u53d7\ua3e6\ud1ba\u1561\uddf1\u7a3f\u9c1c\ud3c0\u45d4\uc71b\ue82f\u9cb4\u3adf\u731e\u3f2f\uf761\u73bf\u0003\u4295\u8f0c\uee8e\u4215\u7670\ubbc1\ue1d8\u540a\u5a70\u5ab2\u807c\u8a29\uba0e\ued91\u373d\u275b\u0d56\u4a2f\uaae4\u2946\u9f27\u6268\u8ae3\u6533\u5792\u25b4\u2077\u7bd5\u0a52\u60e8\u5be8\u1974\ue8e1\u0314\u51af\uc57b\uf0c0\u84d1\u5460\u3672\u1f7a\ufb73\u52c6\u8673\uca3a\u2927\uf44b", 286693912, 0x16AA02A4 ^ CryptoVault.mask()), Target.NONE));
    private static final List<Step> DROPDOWN_STEPS = List.of(new Step(OnboardingTour.xb("\u3f61\ucaab\u037e\ue238\u285a\u6e4e\u123d\u3dc4\uf754\u1241\u083a\u6547\u53b3\uf03b\uf18c\u7327", 2013707277, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u2efe\u1f48\u503e\uef02\u7693\uc4ed\u5f4f\u37e4\udc40\ue994\u7c25\ub586\u1126\uf203\u411d\u5dca\u661b\u4c8c\ud7ff\u20ca\uad0d\u8797\u5e6d\u07c0\ucb3e\u1b0e\ufa33\u977f\ub2c2\uca2c\u6687\uf7ce\ufd5d\ufd57\u61cc\uc9e8\u02bb\uf2fb\ufff8\u9ace\u2212\u60d3\u1e04\u9fcd\u0ada\uc349\ud0b1\uf409\u141b\ub11d\ufd83\u1e7f\u28c8\ueaab\u49ba\u8e8c\u65bf\u830b\u74f6\ued45\u62cf\ub02c\uc151\u672b\uc54f\ufede\uec50\u5ab5\u59ae\ua17e\uecb1\u4766\uff98\u9c5f\u1ade\u1eb1\ufdeb\uf7fd\uab9f\udb92\u746f\ufa11\u8a00\ue0ef\ubabe\u6b82\ua67f\ud897\u73e9\u0226\u6631\u274f\uca75\uc49a\ua7d7\uffeb\uac79\u193d\u1601\u85a9\uc291\uec2e\u7313\u3efd\u564b\u772e\ua73c\u7d12\u4391\u2c25\u7905\u284d\ud5c8\u1485\u5acb\ue417\u7965\uc6d0\udbed\ub7fd\u877d\u19c0\u6293\uf319\ueabf\u3337\u680d\uae56\uc6e5\u5c11\uc584\u4d10\u57d9\ubd9d\u0e09\u63b6\u75ca\ua0e5\u23e9\ue113\u177a\u4627", -1488791799, 0x16AA02A4 ^ CryptoVault.mask()), Target.NONE), new Step(OnboardingTour.xb("\u903d\ue73f\u4760\u5a05\ubdfd\u1b91\u954d\uc987\u1311\ua3bd\u574d\udf8b\u3e3e\u4dab", -1041471552, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u9931\u382a\u16fc\u9fcc\u9575\u1a1c\u5508\ue0ca\u8a2c\udaca\u073e\u6cbe\ub0a3\u0c23\ubf5f\uc62f\uf6cb\uc8d7\u3c98\u357c\u4a5b\u2b0f\uad81\ue02d\u8a1b\ue459\ub110\u5ad8\ua4e8\ue235\u6aa8\u28f1\u36f4\u4536\ub3df\u2805\u8257\u139d\u38e2\u793b\u6f88\u5730\u3f0b\u0ac3\u7d38\ua908\u1f3c\ua7d9\u68e9\u6652\u8603\u5c2e\u3fcf\u663d\u4c3d\uc38e\ud4ae\u96e1\uffbe\u0270\u5e1c\u01f9\u1fe8\u036f\ue59d\u987f\u32cc\u7deb\u6bb1\ud978\u74c7\u94f3\u26f4\uff67\udc28\u8ad0\u7948\u0c3b\uf543\ub65a\ub7cb\ude62\u000f\u8f78\u780e\ub5ea\ubbb1\u2bcc\ua756\uf40f\uae12\u38c6\u67c1\uf7cd\ue232\udc95\ubf22\u551d\uaeb8", 602976653, 0x16AA02A4 ^ CryptoVault.mask()), Target.BAR_UTILS), new Step(OnboardingTour.xb("\u9338\uf042\ubde2\u4161\u54d4\uf33d\u5bec\u249e\u6763\u8489\u2e18\u7ab9\uafbb\ubc00\u3635\u42f0\uee61\ud87b\u3b8f\u5e34", -747520403, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ue3d4\ufedb\ucb5f\u4aae\ua1cb\u6065\ub4d0\ud6fd\uf480\u96c3\u1afb\ub145\u3832\uc0d4\u6df1\u9e13\u1a13\u53a5\u1578\uf86c\u799c\u66c0\u3ce8\u8e90\ua389\u2c70\u36ba\u7d9f\u7cf2\ua35e\u96ed\u2222\u39cc\ue6de\uf839\u68fd\uf314\ub621\u693f\u280d\uda95\u9b82\u7203\ue75e\u1b94\u8965\u36f2\u24b9\ub598\ufefa\uc4c1\uabdf\u052b\u322d\u2fc4\u9e65\u3ec4\u3ee2\u745a\u47fd\u5599\ue5ab\u975e\uf944\ue07a\u584d\u3b85\u628f\uf62b\ufe0a\u3e2d\ucde5\u0e71\u8dd6\uc68f\ud93f\u624d\u35d3\u9bf8\u8899\u815c\ue801\uc279\u83b3\u6c06\u7753\u2d98\uaa73\u2a71\u0a55\ub5a4\ufe1d\u9cb8\uc943\u84de\u769f\u4fc3\u1937\ua2ec\u509c\u1d64\u5cea\u1156\udee0\u02aa\u51e1\u172d\u717c\u8f8d\uf9c9\u6eb4\u2c67\ud0e6\u1a79\ub6ba\ue101\u0537\u3fdb\u10f9\u7811\u6f99\u012f\u6e70\ue1be\u47d2\u2aa6\ud4cd\ubdc3\ub387\u4183\u303c\u7629\u68a4\u252c\u8f04\u5832\u512f\u801c\udf8a\u0481\uc36a\ub20b\ued41\u813a\u000f\u065a\u4d88\u2dfd\uf1fd\ud803\u0410\uf50a\udcec\ue55a\uc07a\ua827\ub082\u5114\ub1ba\ufd6d\u295f\u7536\u2be9\uce2e\u40ee\u9893\uc7c40\ufd5c\ufcc6\u40ad\u0953\u6577\u1c17", -443597456, 0x16AA02A4 ^ CryptoVault.mask()), Target.TOOLSTRIP), new Step(OnboardingTour.xb("\uc5e6\u1144\u3625\u1cdb\uda60\u481e\u0ffb\u5350\u8144\ufb91\u1cdd\u1868\u9575\uf610\u025c\u871a\ubc54", 1802689501, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\uabf0\ufab1\u94cb\ufafe\uf5e6\u0d71\ue42b\u291b\u4d45\u26be\u9dbf\u2885\u0a69\u4590\u92b0\u72ca\ud7d8\u4f1e\u8bb5\uc3e7\u01eb\u9715\u7965\u9273\u492b\u3276\u5e97\uf55d\u78b2\uf0a4\ua01e\u0bba\ua0a5\u0b18\u8367\u6622\u63fd\u0fd7\u903f\u6e76\u0468\u67aa\ub44e\ud79b\u03a2\u58c4\u3294\u9994\u072c\u50a0\u40db\u843b\uf1e4M\u5c9d\ud85c\udf93\uc1db\u0315\u6adb\u1d7c\u0985\u4742\ude46\ucd2e\u0397\ubf44\uf261\uafba\uab95\u2cb2\u0e89\ud072\u6ca0\uba69\u8062\u317c\uaa76\u2b4f\ud21f\u1d9c\uc95f\uc643\u40cb\u9515\u1322\u9c5c\u86e9\u1d99\uba4f\u8cba\u85af\u202b\u4606\u070f\uaa0c\u177d\u143a\uc8bf\u6e9e\ua3e7\ubec7\u731e\u024d\ud5dd\uf9c8\u3a0f\u71dc\ude95\u1da3\u5ec5\u57cd\u79c5\u2765\uee30\ue8d4\uc28e\u70ed\uec50\u88d6", 707655539, 0x16AA02A4 ^ CryptoVault.mask()), Target.PANEL), new Step(OnboardingTour.xb("\u5da5\u4f31\u6be4\uc731\ufe17\ud4ba\u831c\u107f\ud0b6\uef4a\u4de1\ud27c\u26e7\u62b3\uaf88\u8953\u2d64\u978e\u6a6c\uc8e8", -528918008, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u1a19\u1ad2\u5866\uf883\uab0a\u5ccf\u898e\u5911\u089f\ubadf\u7e03\ud704\ub411\u2e88\ud667\u2022\ub67b\u79e5\ud48c\u3dcc\u9198\u3598\ue450\u36b1\uf5fa\u53f2\u2074\u25c6\u7c4c\u4305\u8ef2\u6e8c\uaf9f\udb49\ue935\ufc13\u0d8a\u5e15\ucea5\u4102\ue5fe\ua32e\ua5cb\u458a\u6f77\u17b0\u08c6\uc35e\u266a\u0e57\uad83\ua925\uf64f\u0c39\ua1e5\u4c4c\u9c7b\u4280\u164f\u0837\u6292\u431a\u5eca\u81f0\u71ce\u9d2d\u376e\u412f\u43e8\u875d\ucfc1\ucfe3\ub95a\u6164\u5657\uaeed\u3cbb\u9555\ubfa7\ub6bd\u7c96\u1f81\u987b\ub6bf\u44d7\ubcb9\u53d1\ueea9\ud155\u4879\ubd8a\u87e5\uce2e\u66d7\u1ee8\u98e3\uaf86\u60d1\u4a60\u3097\u6a98\u64f6\ua846\u6ec1\u5101\u670f\ude9f\u3813\uf447\u042c\u5b9b", 1652549305, 0x16AA02A4 ^ CryptoVault.mask()), Target.PANEL_CHEVRON), new Step(OnboardingTour.xb("\uade9\u2aba\u8c92\u9aa8\u53c0\u2752\u49a6\u9eab", -260539236, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ucb22\u3f04\uf58b\u8426\u5025\u8004\u7269\u4dea\ufaab\ube75\ubb9a\u8eab\ub6d8\u5363\u1de9\u4847\ua145\u25d6\ue7a4\uc4ec\ue348\u6954\uc709\uf41b\ud6bc\ub806\u3ab0\ud0b8\ude31\u617a\uff2e\u3574\u5361\u3670\uf473\u26be\u417e\u7915\u146e\u45bc\u448d\u16aa\u1efc\uf5b8\u3180\u15f2\u7743\u14fc\uf1fb\u3246\u6857\u9b96\u3798\ub3ca\udb28\u0928\uc83e\u36f8\u6f32\ue4e9\u16fc\ubbee\ud9c4\u58ed\u72ab\ude01\u29b8\u6c9c\u8715\u2482\u750d\uf9d2\u6257\u45c1\ub0b9\ud407\ufd37\ub067\u8bbb\ub854\ud000\ub4be\ud782\uf966\ud7b6\ub6d4\ud4f4\u70ca\u0903\ubebf\u85ee\u3be5\uc1d8\uaad6\u1edf\ua149\u703d\u6779\ufb9c\u1e94\ua1c6\ue72e\u9b2f\ub5f5\ud82c", -196836941, 0x16AA02A4 ^ CryptoVault.mask()), Target.MODULE_ROW), new Step(OnboardingTour.xb("\u78df\u6f61\ue70f\u8294\ub9ca\uaef1\u05f2\ue28b\u61b4\ua513\u1ddd", 376673002, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u7025\ufe04\u0965\uf86b\u8cfb\u523c\ue6ab\u3184\u0ecc\u41ea\u2a01\u3aac\ucc1d\u023d\uc97f\ucf56\ucfb8\u0888\u0e18\u8cf3\u9a4f\u0a8f\uf838\uecad\u49fc\u5e8d\u7ddf\u6c63\u8a27\u15e4\u5b08\u3744\uec2b\ue5da\u64e7\u28dc\u2654\ue234\u7e30\u50c7\u7e3e\u32c7\u74a3\ue894\ua1e3\u4b75\ua43d\uefe8\u956b\ue656\u1b84\ue0c4\u693e\ua03b\u0d85\uac55\u4c1b\u0348\u4be3\u53c0\u6d21\u5f0b\u3d62\u1ded\uf91a\ua666\u827a\u330b\ue91e\u3b2b\u6941\ufe8c\u3148\u8c0f\ud2c5\uaf4f\u4102\uff80\u2863\u1ad0\ubb0c\ucc1e\u831a\u1697\u1d90\u0d8e\u5e4c\u11bb\u6db5\u640a\uc72c\u1a77\uca38\u147b\u8d47\uf510\u2816\u4b58\ub164\u87e8\u2866\ue3fa\u18b2\u757e\uf220\u2162\u695c\ua2eb\u6f6c\u3aa2\ua84e\u15a2\u056c\ua0f6\ucc29\udc1a\uf0fe\u2868\u8c06\uf889\u3ec9\uabaf\u031e\ud995\u4c17\u1288\ua5df\ud0da\u9c7a\u2629\uf79e\ud8d0\uee95\u4e00\u31fb\u5a35\u1e32\u1ea2\u8444\u0d2a\u8e90\u533a\u9f2b\u3b16", 1892014389, 0x16AA02A4 ^ CryptoVault.mask()), Target.MODULE_CHEVRON), new Step(OnboardingTour.xb("\u4332\ub436\u935c\u018d\ufe3d\u94d8\uad11\uda30\u8b47\u3de7\uf1f5", 1102944620, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u37e2\uae2d\ub0e4\u1a77\u2cd3\u5ca4\u7c3e\u298d\u5f1a\u9ea6\uda07\ua132\u23ae\u8be6\u3f45\u9b44\u3d68\ua8c7\u4fc1\ud75c\u9481\u1c85\u1f4b\ub8e4\ud01e\ue870\u3509\ubb03\ufc74\ub596\u23c9\u0e9d\u771e\u96c1\uf7a2\ufe28\u8f22\u6eef\u7057\uef53\u6ca8\u365a\ubb60\uaed6\uc724\udabe\ub258\u45d8\ub777\u8876\u9c84\u8abe\u983e\u309b\ucf1d\u0e35\u60f8\u478f\uc4b8\u119b\u9617\u337b\u25c8\uc37e\u7cf8\ud516\u3dab\u1ba1\u6f1c\ue1d0\uc814\ud684\ub349\ubadfv\uef6b\u040c\uebb7\u09f7\uf9f5\u43dc\u7389\u4b7c\u04c2\ue0f2\u8675\u587e\ud88f\uedfc\u00ee\u9b6a\u6a13\uf905\u3847\uad27\u32da\u930e\ufe65\ua903\u0849\uec24\u529b\u84fc\uaba8\u8064\uf62e\ub1a7\u07c6\u329f\u84e6\udc57\udea5\uf8fd\ufdce\u776f\uc5f0\ua989\u027a\u858e\uc197\ub391\u881d\ub8b2\u82d4\u14a9\ud125\u8bda\u0427\u09f9\u4f2e\u563f\u1ae1\u0f60\u95ff\u257f\u2b4e\u6c73\u15ea\u79c7\ubbef\u7998\uf049\u207e\uea5c\u04a0\u9be4\u5a5f\ud842\uc6dc\ufe94\u6fb8\ubef9\uc6ca\u7baa\uecf5\u11f9\ub94f\ud336\u9afd\u4e98\u2db0\u54a5\u9747\u6452\ub893\u665b\uf228\ud1e8\u11a1\u13a1\u09a5\u1ba6\u749f\u6594\uf3ba\u3810\u1d12\u2954\ued39\ue51c\uab3d\u1f80\u14a5\u564e\ub9f6\uca45\uccd3\u0bfe\ue3f2\u23af\uf441\u9acd\ud68a\uc7ff\u8e01\uac63\u79fe\u8266\ue317\u9d07\u0d99\ue8eb\u3db5\ufbf8\ud763\uf89b\u4633\u5970\u515f\u7895\uecfa\u1605\uce2b\uc454\ud501\u7d09\uc3be\uf6f2\u8455\u52a9\u3fb5\u29cf\u522e\u5dcc\u71ce\uead3\u4168", -355961613, 0x16AA02A4 ^ CryptoVault.mask()), Target.MODULE_CHEVRON), new Step(OnboardingTour.xb("\u3d88\u80e7\u539a\uda58\u0ed0\u7a7e\ud3bc\u77e9\u5579", -2062739999, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u1521\ub03a\u3bd9\u477c\ud4f9\uf20a\u05f3\u3aac\ubaa8\ub70b\u6eff\ud9a3\u1329\u5e8c\ub07d\uace4\ua9cb\ub6aa\ue601\uaa4c\ua8d8\u3224\u7262\u084c\u9f48\ua7bc\ub896\u11c1\uca60\u7093\u5ea8\u8683\u0688\uc119\u7101\u8798\u94ce\u9703\u87d4\u92af\u7e62\ua5e5\u62d6\ue67a\ue3bf\u7a08\ud10d\u6e0e\u80e2\u8cc5\u97e6\uf797\u3106\u37e5\ud502\u0e53\u45bc\u84d9\u1f6c\u5123\u3aa5\ua9d5\u1bcd\ua2bb\ubdf4\ub965\u752f\ufee3\u0f73\u5321\uffaa\u8644\ue090\u35ff\uf343\u78b5\ucfde\u6857\u1b93\u2c33\u14a2\ud9f5\u422e\u6654\u78ba\uad75\u5aa7\u409e\u32eb\u87e2\u9ace\u82e3\u1d7d\ue42d\u8b30\ufbf5\u4541\u80d3\u9355\u997d\u614f\ud121\u036f\u0783\u9cbf\u1abd\u52c4\u36f6\u564f\ud647\u3d89\u8406\u03b5\udcf9\u9ba9\u4d1b\u631d\u8e91\u0957\u1214\u483c\uffdb\u1b85\ue789\u05a8\uebea\ub58d\uc0f3", 1622749739, 0x16AA02A4 ^ CryptoVault.mask()), Target.BAR_CONFIGS), new Step(OnboardingTour.xb("\u1913\u9363\u16db\u6267\ud5d9\u6a39\u22ea\u6509\u28bc\u3b75", -1350637478, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ucc4e\ubaa3\u637e\u0c51\uef96\u876c\ud9a2\u015a\u0278\u92a1\ub6dd\u4eab\udf97\u4ece\u1d1a\ucda1\ua94f\ua2b5\u8dd2\ua46b\u4170\u7232\u797a\u59b8\uc026\udeb3\ub728\u303d\u26f7\ud44f\u3905\u1229\u4756\u93df\u8415\ufeaa\u6534\u8dd3\uf63c\u5c06\u8af0\ueccd\uda8f\u2436\u223a\uccfd\u037e\u5e1e\u4dad\u4553\u8ff8\u3112\u368f\u1113\u70a9\uaa22\u98da\u45e8\u6db8\ua128\ud24a\u0140\uea3a\u290d\ue838\uae55\u2f03\udece\u61fa\uf932\ue4b3\u24cc\uc3da\ua370\u258c\uc83b\u1079\ufc45\uce84\ub43e\u65ae\u5d07\u9e03\u57bd\u30b8\u03f1\u5f72\uf3fd\u10e5\u49f8\u4565\ua053\u4e98\u40da\u5374\uf278\u24b3\u3144\u5e8c\u8750\u0dd4\ue0e5\uaf3d\u8bf6\u850c\ud5ce\u161c\ue76d\ueeef\ub8f0\u2bda\ue93a\u809f\ue049\u93f6\ub090\uc9f5\u8561\ueff1\ub15f\u7a2c\u3d8b\udaeb\u963a\u1344\u9153\ue928\u9c85\u3536\ucc6a", -1334341655, 0x16AA02A4 ^ CryptoVault.mask()), Target.BAR_MACROS), new Step(OnboardingTour.xb("\ua933\uca84\u0f1a\ua433\u7840\u3c6b\u2a27\u0c2f", 387398065, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u6233\ue2f5\u427a\u0ddf\u0d46\u4aa4\u7022\ua34d\u01e4\u437d\uaa57\ud778\u5879\u0f29\ueb96\u5bf8\ua372\u39b3\u221f\uf020\u3ee6\ua88d\u144a\ua90b\u1c09\u39e2\u27c5\u9bfc\ub319\u4fda\ub889\u65c4\u8be5\u4c18\udaf7\u33d2\u50c7\u45dd\u7feb\u326c\u9a55\ua8f5\u5ca3\u9ce7\uab36\u8373\uf10e\u0f59\uac27\u1be3\ub786\u35c7\u0bd8\u5502\ued65\uab8a\u22cf\u3017\u2338\ucdc5\u7413\u2d7e\u6f33\u2dba\u52b8\u2e25\uf247\u72fd\u9acf\u1973\ueae7\ud32f\u0aba\u4fbe\u06fe\u04e7\u9b0b\uea2a\u2c61\u4ee7\u41f1\u36f6\ucc73\uc47d\u6fe9\u0754\ua92b\ua4e5\ufbff\u1dee\u128e\uaac5\u4f8c\u680b\uf39d\ud278\u5a3f\u5f97\u0314\u5505\u1eb2\u64fc\u8cdb\u66d6\u996f\u69ae\u9f95\u5653\u95f4\u2cbd\uc1ed\ue6d0\ufed0\u1678\u84f4\ud9c2\u3582\u7b9e\ube7f\u1660\u6e34\ubc7d\u22b7\uc6fb\u5360\u2ff4\u2227\u2dd8\u5795\u05ac\u3493\uda5f\ub255\u2a68\u3c12\u658b\ua654\ubec8\u74b6", 1585918539, 0x16AA02A4 ^ CryptoVault.mask()), Target.BAR_SCRIPTS), new Step(OnboardingTour.xb("\uc587\u585c\u3606\uda52\u2767\ub16d\ubcb2\u184c", -119243205, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u88ba\u6cdb\u3dd4\u985b\u6edf\u3d1e\u961d\uc31c\u7236\ufc21\ua8be\ua1a9\ua4d9\u3f18\u2d21\u637c\u6991\ubf86\u11cd\u332a\ucb12\u3c79\u9be3\u58fe\ud02c\udfc6\u2959\ucc24\u1856\u3b38\u69c4\u2248\u0eea\u1d98\ue8ed\u962c\u8250\ubd70\u8c29\uba6c\ub6ac\u0fd4\u3be9\u83c6\u6d0a\u19e3\u29a2\u5460\ua181\u849c\u7e3c\u0015\u3c25\u8086\u2d17\u350d\u241b\u91b8\u9259\uf7b7\ube02\u7ab5\u5550\u3bf1\ufcf2\u4a64\u32d0\u332a\u08b8\u8a6b\u41e3\u14d2\ueb09\u0a73\ufd73\ua196\ua76e\u5ddd\uca3b\u6dbb\ub8a3\uae52\u27d8\u3757\ub4c8\u9f1b\u77aa\u08fd\u5ed3\u4422\uc0d2\ue5c1\ub886\u039a\u6ffe\u94f4\u5e72\u770e\u373b\u7fe3\uc2c0\u1c38", -905807474, 0x16AA02A4 ^ CryptoVault.mask()), Target.BAR_PEOPLE), new Step(OnboardingTour.xb("\udfbc\u36e8\u86ce\u904a\u5f89\uc938\ub862\u6667\u7d34\u02e9\u76bb\u9c29\ud9e2", -2133631850, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u4183\u3e89\u63f2\u94dd\u29e9\ubb89\ubae7\u7873\u690f\ud0ea\u7c27\uf293\u09f2\uaffc\udae9\u66d0\u11ba\u6001\u6abe\uffa9\uf978\u821d\u3ef2\u7039\u0316\u2dcf\ucb05\u578c\u3e06\ucfc6\u8e7e\u73de\uc2fd\u9d1b\u9cdf\uaca1\ucf6e\ufc00\u1079\ue08e\u22b7\ub83d\u0a66\u0842\u318a\u0509\ud466\ua8b4\u19ca\ubd7e\u49d4\ue77d\u02e6\ud53c\ub45c\u6733\uf213\u145d\u373a\ue6c3\ub7f3\ucf47\u8a58\u6dbf\uc939\u9056\ua124\u2291\u1a8c\ucb94", 1716009380, 0x16AA02A4 ^ CryptoVault.mask()), Target.BAR_COSMETICS), new Step(OnboardingTour.xb("\u8982\u6d03\u47e1\ua5c4\ua12a\u7fa2\u2c8a\u991b\ubc1d\u0ca8\ub3a9\uf358\u1263\ue8f7\u6644\u1252", 751248026, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ub1c0\ua710\ue20f\uee93\u7f7a\u762f\u8ba1\u7f96\u326d\u95f1\u7b56\ubc8f\u4732\ufc1c\uac9a\ua642\ud0f7\ua89e\udb6b\u54f4\u0893\u3a76\ud6fc\u9d17\u32b8\u2424\u01c9\u7749\uaab7\u8eac\ub2e4\u15e2\ue0ff\uc142\udb76\u2171\u5fca\u48f8\u85de\udd5b\ufacb\uc499\u6ca9\ue8ae\ub115\u4d1e\u1bc8\udf06\u902a\u6999\ubcf3\uc90a\u2104\ua5be\u59ab\u6575\u608c\ufba8\u426c\ue54b\u8253\u379d\ucb44\ua2b3\u48a0\u9d6f\ud20f\ue51b\u2ae7\ubfa6\u70c2\u55ea\ub54b\u1570\ud56d\u5a3d\u6bdb\u5ad1\ub327\uf4a5\u0185\ua284\ufb0e\u9fb0\u7880\ub274", 811190796, 0x16AA02A4 ^ CryptoVault.mask()), Target.BAR_BINDS), new Step(OnboardingTour.xb("\ubc0e\ucfcd\ued9d\u42da\u92be\uc0ba\ub909\ubb32\u9bc7\uf7fa\u6307\u0249\ub250\u3c45\u5d3f\ufb12\u4d0f\ua974", 1091389438, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u2eff\u99e5\ub2ea\u9512\u179f\u0300\u35ae\ud838\u9727\u9107\u1b7f\ued67\u1b52\u9cca\ua011\udfe8\u0aca\u9fd6\u750d\uc1c5\u5408\u2758\uefb3\u2986\u9def\u2771\u35e7\u3dd1\u9e96\u8960\ub811\u7048\uc7a7\ud1d4\u29af\u862f\u9160\u4d9b\udce8\u63f7\u0de5\u7191\u79db\uec8b\u11a5\u0dbe\uccaf\u0bf8\udae5\ub1e9\u96c4\uae54\u1103\u7a95\u0ee3\u30ed\u0b1f\u2ea1\u3f2f\u032f\u5e86\u6e9b\ubeaa\u492f\ue47d\u8ad3\ue8cd\u18d3\udd4f\u0fa3\ub2c4\u986f\u1c39\u8ffd\ubebf\ue4c6\ua135\u62f4\u22d1\ud0d6\u6687\u47ff\ue84e\u414f\u03ad\u2a36\u1915\u6f1d\ucde2\ua8b1\uba64\ufbbb\u370a\u5da6\u0f15\u343d\u8c89\u0cbf\uf311\u8f11\u0834\u9324\u3c40\ud03f\ub645\u7ef3\uc0d7\uc503\u7cb3\uf415\u0f75\u63d1\u2738\uad72", -211585127, 0x16AA02A4 ^ CryptoVault.mask()), Target.BAR_SS_PROOF), new Step(OnboardingTour.xb("\u4445\uf929\u0875\u4a4d\u64d5\u5ad4\u9fca\uaeb4\ua4a8\ucbc7\uf1a0\u50fa\u0491\uf2ab\ud3eb\u81f5", 605008217, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ub9e6\uc1a5\u4db9\ub099\ud82f\u83af\u81e2\u95ac\uc060\uf57f\u4eef\ub9e9\ufeaa\u71cf\uf8fa\u8827\u9fa5\u1666\uf784\u5c40\uc0d1\ube61\ue533\uc465\u5ba9\u0905\uf16b\u6065\uee66\u5ceb\u7f1d\u58c4\u6be7\u9d1b\u3f8e\u25a5\ue83e\u6f22\u1ec9\u1f5f\u7912\udc3f\ufe9f\uae6d\ue154\u2475\u2353\ue9ff\uce93\u7fae\u6655\ucefe\u1a77\u23a1\u1e5e\u786e\u4d26\u3601\ubb49\ucb92\u00e2\u9a40\ufc6f\ud824\u8534\uae73\u304c\u1871\u3f03\uad37\u6786\u9583\u46ec\u1f7f\u4142\ua6d3\u4957\u56c4\u7eb2\ufbfe\u75c3\u393b\ub66b\u7ecd\u6ee8\u9942\u9341\u9856\u2a7c\u04ad\ub5f2\ud994\u9800\uf2d1\u70ae\u0b39\ud476\u2237\u96eb\uc0dd\u06ee\uef8a\u2f20\uac3b\u9180\u0e4d\uaa77\u0db9", -2147263351, 0x16AA02A4 ^ CryptoVault.mask()), Target.BAR_SELF_DESTRUCT), new Step(OnboardingTour.xb("\ucd2b\ucae8\ufa71\u8d08\u1eef\u069f\ue88e\u465e\u31c6\u8df8\u5e52\ubc34\u0213\uedfd", 1396809733, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u0617\ubcdf\u3476\u9468\u0d32\ud8a7\ub2b7\u7b31\u2370\ucd6f\u08ab\u538f\u3fe0\u9cfb\u5db2\u810a\u65bc\ubb1d\u4897\u5a35\u6312\ub4a6\u0952\udb73\u45d7\u275e\ub18a\u40ea\uebf3\ubaa1\u7f6b\ud304\ucba1\ueaa0\u3553\u09f9\u195b\uac4f\ubf48\uebec\uaee4\u799a\u283c\u1b2a\u4cee\u7d67\u762e\uecbe\u27ab\ud68b\u7f80\uc7c1\u7b40\u795d\ufe38\u5ca7\ue5dc\u364b\u6fef\u49ef\u838e\u7316\u7cc2\u6390\uc60d\u2f92\u018a\ubbb2\u18c0\ucd85\ubebe\udb1d\ub650\u35f9\u3f00\u309d\u9f2b\u0ca9\ue63f\u100b\ue9fc\ueb0b\u5bb5\u8b3f\u63db\u59e2\u9d53\u8d2e\uf94c\uf73b\u6d5f\ua2bb\udd2a\u0ddf\uc5dc\u1ed7\uc18f\u147e\u0bcc\ub4d1\ua530\uab42\uedf5\u699a\u2b1e\u0125", 697823332, 0x16AA02A4 ^ CryptoVault.mask()), Target.BAR_LAYOUT), new Step(OnboardingTour.xb("\ua1d6\u1988\u3583\u5103\ua602\u3417\u9ad7\ufbb2\uf882\u8a2d\u01f3\u226a\u7a32\u23cb\u9d08\u8f28\u6fe4\udc91\udb47", -1133144871, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\ude7c\u5d5c\u0087\ucdb7\uada7\uad60\u47e1\u2a9b\u290c\u2983\u73ca\ua4d9\ued5e\ua067\u95ac\u6785\uce55\ua8fd\u24d2\uf65d\u4fe3\ue394\ub887\ufb33\u26b4\u4648\u0693\uaa21\u4cf7\u36c0\uf838\u86ba\u1f5c\u2004\u386e\ue467\u9d2e\u5bd8\ue763\u1ce3\u043f\u0335\u4b5a\u7023\ucefa\ubb82\uc31b\uf897\u4c83\ubc85\u9544\u85f1\u8f76\ub15b\u08ab\u7ee2\ud96f\u1f6c\ud281\uf570\ufea7\ub24a\u08bb\ufa03\ub457\u1298\u20e0\uca4c\ue27d\u9677\u53c3\u8532\u7d41\uc66a\u84f4\uad2b\u4ad5\u2c86\uf7b5\u3589\ua650\ub87d\u5e5b\u09b4\ufdfa\ue53c\u3fae\uc433\ub3dd\u2b11\ue88a\ud5b6\uaa75\ua4f0\u6061\u2052\u076c\u19fe\u520d\u4d8b\u319d\u337c\uea78\u9310\ud043\u5dc9\u6a2a\u5bcd\ufd61\ubc37\ueef6\u9238\u481a\uc10b\ue1f0\u951e\ufec2\u3d7c\udde1\u1461\u954f\u687c\u5225\ua523\ua744\ud880\ud172\u3d28\u5d17\udfd0\u57da\uc314\u9cf1\ufc84\u265f\ufb44\uf8b9\uf4c9\ueb2c\ufb21\uf4ac\u452c\u6fb0\u4778", 2083133283, 0x16AA02A4 ^ CryptoVault.mask()), Target.TAB_CLICKGUI), new Step(OnboardingTour.xb("\u834e\u76ff\ufa23\u5724\u8de9\u74e9\ud591\u3c5a\u5257\u1c7d\u7be5\u7652", 685751673, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\u4c67\u6210\uc11c\uf4fd\u96d3\u96db\uc63e\u5563\u4fdd\u1818\u2822\u6ec1\uf9f1\ub0a5\u218d\ud437\u3dc6\ucb3c\ufcfa\udb4d\ua8f5\ud1bf\ud253\ud2e2\udab7\udc8a\ud077\ue8e1\ua7ba\u47b3\u8e21\u21b3\uab56\u1879\udc07\u42de\u4eb7\ub7ab\u9e1e\u192f\u78de\u008c\u51c3\ua71e\u4ab8\ucc32\u1230\u26e6\ube07\u7dbd\uedb3\ud75b\u918f\uac73\u17ff\u06c7\uf13b\u9101\u45a5\ufa95\u2b2f\u4c73\uebe6\u7a21\u929f\ue5f6\uf407\u11c2\u67de\u3e7f\uc6c3\ufdb7\ubdb0\u2f22\u28ee\ufd55\ub62d\u01d9\u6b36\u0569\u250d\u4e49\u5dd4\uad32\udc44\u3631\udaef\ua05a\u9c08\uc591\u4ec6\uec24\u3c64\u5387\u5db6\u2b3c\u7196\u019f\u2827\ueb85\ud92e\u7a54\ue6ab\u28e8\uc711\u5f9a\ue495\u66ef", -95006501, 0x16AA02A4 ^ CryptoVault.mask()), Target.TAB_HUD), new Step(OnboardingTour.xb("\uc207\ub599\uc1fb\u8ed5\u8f89\u81ad\uf10e\u10be\u1e70\u40f0\u0793", -560114274, 0x16AA02A4 ^ CryptoVault.mask()), OnboardingTour.xb("\udb04\u13ae\u3f2c\u6c74\u6d98\u2bb5\uaade\uac52\u3554\u9022\u7ba3\u31fe\u34ef\uf156\udb77\uaa9f\ud2cf\u45ab\ubb68\ua19e\ue7c3\u5e87\u787b\u4634\u04a7\u1ce3\uc46d\u27f9\u3423\ub17f\u2d94\ue158\u99c1\u0937\u090a\u8c71\u9927\ub3db\u9f2c\uf514\ue368\udae0\u1841\ufe3a\u4512\ue42c\udf51\ufe7a\u2f75\u5c5f\ue4a9\u0e3c\u878e\u1f39\u90f1\ub10e\u9743\ub482\uab98\ue282\u967d\uec87\u281d\u5ccc\uf931\uacd7\u425b\u1913\uf053\ucbe0\ub1dd\u8862\u4140\uea22\u11e4\uaa06\uc799\ub576\u99a1\u502a\ua132\u6212\u2a58\u82c9\u5030\u06fa\u3584\ude6d\ubcee\uf473\u9c5d\u4ff8\uaf2d\u70fe\ud7c6\u7f1b\u40af\u4cdc\uf199\ue252\u2c28\u92dd\u2c26\u0829\u9a2a\u1aca\u741d\u1b86\u83f3\ufce9\u2101\udf04\ub1b9\u6c1a\u3e0b\u54b8\u3767\u3053\u5d07\u86a5\u0b7a\u2757\ud7ea\ufdff\ub94a\ue038\uac2f\u75d7\u58a9\u1afa\udeb0\u3758\u0825\ub27e\u6ec3", -101747437, 0x16AA02A4 ^ CryptoVault.mask()), Target.NONE));
    private static final Set<String> SHOWN_THIS_SESSION = new HashSet<String>();
    private final List<Step> allSteps;
    private final Predicate<Target> visible;
    private final BooleanSupplier seen;
    private final Consumer<Boolean> markSeen;
    private final String id;
    private int step = -1;
    private boolean autoChecked;
    private final SetupPicker picker = new SetupPicker();
    private SetupPicker.LayoutPainter layoutPainter;

    public void setLayoutPainter(SetupPicker.LayoutPainter layoutPainter) {
        this.layoutPainter = layoutPainter;
    }

    public boolean mouseDragged(double d2, double d3) {
        return this.picker.active() && this.picker.mouseDragged(d2, d3);
    }

    public void mouseReleased() {
        this.picker.mouseReleased();
    }

    public boolean scroll(double d2) {
        if (!this.picker.active()) {
            return this.active();
        }
        this.picker.scroll(d2);
        return true;
    }

    public OnboardingTour() {
        this(PremiumGate::studio);
    }

    public OnboardingTour(BooleanSupplier booleanSupplier) {
        this(OnboardingTour.xb("\uf9d2\u7327\ue5b0\u2033\u9208", 1370050233, 0x16AA02A4 ^ CryptoVault.mask()), STEPS, OnboardingTour.menuVisibility(booleanSupplier), () -> ClientPrefs.get().isOnboardingSeen(), bl -> ClientPrefs.get().setOnboardingSeen((boolean)bl));
    }

    public static OnboardingTour forDropdown(BooleanSupplier booleanSupplier, BooleanSupplier booleanSupplier2) {
        return new OnboardingTour(OnboardingTour.xb("\u207b\ub54a\u411b\ubc6d\u1e62\ua209\u9d87\u3a7a\u282f\ub22e\uaf68\u1a3c", -311396843, 0x16AA02A4 ^ CryptoVault.mask()), DROPDOWN_STEPS, target -> switch (target.ordinal()) {
            case 12, 13 -> OnboardingTour.safe(booleanSupplier);
            case 15 -> OnboardingTour.safe(booleanSupplier2);
            default -> true;
        }, () -> ClientPrefs.get().isDropdownTourSeen(), bl -> ClientPrefs.get().setDropdownTourSeen((boolean)bl));
    }

    public OnboardingTour(String string, List<Step> list, Predicate<Target> predicate, BooleanSupplier booleanSupplier, Consumer<Boolean> consumer) {
        this.id = string;
        this.allSteps = list;
        this.visible = predicate;
        this.seen = booleanSupplier;
        this.markSeen = consumer;
    }

    private static boolean safe(BooleanSupplier booleanSupplier) {
        if (booleanSupplier == null) {
            return true;
        }
        try {
            return booleanSupplier.getAsBoolean();
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    private static Predicate<Target> menuVisibility(BooleanSupplier booleanSupplier) {
        return target -> target != Target.RAIL_MACROS && target != Target.RAIL_SCRIPTS || OnboardingTour.safe(booleanSupplier);
    }

    private List<Step> steps() {
        ArrayList<Step> arrayList = new ArrayList<Step>(this.allSteps.size());
        for (Step step : this.allSteps) {
            if (!this.visible.test(step.target())) continue;
            arrayList.add(step);
        }
        return arrayList;
    }

    private Step current(List<Step> list) {
        if (this.step >= list.size()) {
            this.step = list.size() - 1;
        }
        return list.get(Math.max(0, this.step));
    }

    public boolean active() {
        return this.step >= 0 || this.picker.active();
    }

    public Target target() {
        if (this.picker.active() || this.step < 0) {
            return Target.NONE;
        }
        return this.current(this.steps()).target();
    }

    public void start() {
        this.step = 0;
        this.picker.begin();
    }

    private void startAuto() {
        this.step = 0;
        if (SetupPicker.shouldOffer()) {
            this.picker.begin();
        }
    }

    public void maybeAutoStart() {
        if (!this.picker.active() && SetupPicker.hasPendingResume(this.id)) {
            this.autoChecked = true;
            SHOWN_THIS_SESSION.add(this.id);
            this.markSeen.accept(true);
            this.step = 0;
            this.picker.resumePending();
            return;
        }
        if (this.autoChecked || this.active()) {
            return;
        }
        ClientPrefs clientPrefs = ClientPrefs.get();
        if (clientPrefs.isOwned()) {
            this.autoChecked = true;
            boolean bl = SHOWN_THIS_SESSION.contains(this.id);
            if (OnboardingTour.returningUser(clientPrefs)) {
                if (!this.seen.getAsBoolean()) {
                    this.markSeen.accept(true);
                }
                return;
            }
            if (!this.seen.getAsBoolean() && !bl) {
                SHOWN_THIS_SESSION.add(this.id);
                this.startAuto();
                this.markSeen.accept(true);
            } else if (bl && !this.seen.getAsBoolean()) {
                this.markSeen.accept(true);
            }
            return;
        }
        CloudConfigSync.Status status = CloudConfigSync.getStatus();
        if (status == CloudConfigSync.Status.SIGNED_OUT || status == CloudConfigSync.Status.FAILED) {
            this.autoChecked = true;
        }
    }

    private static boolean returningUser(ClientPrefs clientPrefs) {
        if (clientPrefs.isOnboardingSeen() || clientPrefs.isDropdownTourSeen() || clientPrefs.isSetupPickerSeen()) {
            return true;
        }
        if (!clientPrefs.hudLayoutNames().isEmpty()) {
            return true;
        }
        try {
            if (HitboxConfigScreen752809.V != null && HitboxConfigScreen752809.V.getValue()) {
                return true;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return false;
    }

    public void next() {
        if (this.picker.active()) {
            this.picker.next();
            return;
        }
        if (this.step >= this.steps().size() - 1) {
            this.finish();
        } else {
            ++this.step;
        }
    }

    public void back() {
        if (this.picker.active()) {
            this.picker.back();
            return;
        }
        if (this.step > 0) {
            --this.step;
        }
    }

    public void finish() {
        if (this.picker.active()) {
            this.picker.skipSetup();
        }
        this.step = -1;
        this.markSeen.accept(true);
    }

    public void draw(UiDisplayList.Builder builder, UiHost uiHost, float f2, float f3, float f4, float[] fArray, float f5, float f6) {
        float f7;
        float f8;
        float f9;
        float f10;
        if (!this.active()) {
            return;
        }
        if (this.picker.active()) {
            this.picker.draw(builder, uiHost, f2, f3, f4, f5, f6, this.layoutPainter);
            if (this.picker.consumeLayoutSwitch()) {
                this.step = -1;
            }
            if (this.picker.active() || this.step < 0) {
                return;
            }
        }
        List<Step> list = this.steps();
        Step step = this.current(list);
        int n2 = -1509949440;
        uiHost.hit(0.0f, 0.0f, f2, f3, () -> {});
        boolean bl = fArray != null && fArray[2] > 0.0f && fArray[3] > 0.0f;
        float f11 = 0.0f;
        float f12 = 0.0f;
        float f13 = 0.0f;
        float f14 = 0.0f;
        if (bl) {
            f10 = Math.round(5.0f * f4);
            f11 = Math.max(0.0f, fArray[0] - f10);
            f12 = Math.max(0.0f, fArray[1] - f10);
            f13 = Math.min(f2 - f11, fArray[2] + f10 * 2.0f);
            f14 = Math.min(f3 - f12, fArray[3] + f10 * 2.0f);
            builder.rect(0.0f, 0.0f, f2, f12, 0.0f, n2);
            builder.rect(0.0f, f12 + f14, f2, f3 - f12 - f14, 0.0f, n2);
            builder.rect(0.0f, f12, f11, f14, 0.0f, n2);
            builder.rect(f11 + f13, f12, f2 - f11 - f13, f14, 0.0f, n2);
            f9 = Math.round(10.0f * f4);
            builder.border(f11, f12, f13, f14, f9, Math.max(1.5f, 2.0f * f4), UiKit.withAlpha(uiHost.accent(), 224));
        } else {
            builder.rect(0.0f, 0.0f, f2, f3, 0.0f, n2);
        }
        f10 = Math.round(18.0f * f4);
        f9 = Math.round(Math.min(360.0f * f4, f2 - 32.0f * f4));
        float f15 = 10.5f * f4;
        float f16 = Math.round(15.0f * f4);
        List<String> list2 = UiKit.wrap(step.body(), f9 - f10 * 2.0f, f15, 6);
        float f17 = Math.round(30.0f * f4);
        float f18 = Math.round(f10 + 18.0f * f4 + 12.0f * f4 + (float)list2.size() * f16 + 16.0f * f4 + f17 + f10);
        if (bl) {
            f8 = f11 + f13 * 0.5f - f9 * 0.5f;
            if (f12 + f14 + 12.0f * f4 + f18 <= f3 - 12.0f * f4) {
                f7 = f12 + f14 + 12.0f * f4;
            } else if (f12 - 12.0f * f4 - f18 >= 12.0f * f4) {
                f7 = f12 - 12.0f * f4 - f18;
            } else {
                f7 = f12 + f14 * 0.5f - f18 * 0.5f;
                f8 = f11 + f13 + 12.0f * f4 + f9 <= f2 - 12.0f * f4 ? f11 + f13 + 12.0f * f4 : f11 - 12.0f * f4 - f9;
            }
        } else {
            f8 = (f2 - f9) * 0.5f;
            f7 = (f3 - f18) * 0.5f;
        }
        f8 = Math.round(Math.max(12.0f * f4, Math.min(f2 - f9 - 12.0f * f4, f8)));
        f7 = Math.round(Math.max(12.0f * f4, Math.min(f3 - f18 - 12.0f * f4, f7)));
        float f19 = Math.round(14.0f * f4 * uiHost.cornerScale());
        builder.shadow(f8, f7 + 2.0f * f4, f9, f18, f19, 14.0f * f4, 0.0f, Integer.MIN_VALUE);
        uiHost.panel(builder, f8, f7, f9, f18, f19);
        builder.border(f8, f7, f9, f18, f19, Math.max(1.0f, f4), UiKit.withAlpha(uiHost.accent(), 112));
        uiHost.hit(f8, f7, f9, f18, () -> {});
        builder.text(f8 + f10, f7 + f10, 14.0f * f4, uiHost.titleColor(), UiKit.fit(step.title(), f9 - f10 * 2.0f - 50.0f * f4, 14.0f * f4, UiDrawCommand.TextWeight.SEMIBOLD), UiDrawCommand.TextWeight.SEMIBOLD);
        String string = this.step + 1 + OnboardingTour.xb("\u6349\u8fe5\u4c06\u21e0\udf7e\ue0bd\uc211", -1621313459, 0x16AA02A4 ^ CryptoVault.mask()) + list.size();
        float f20 = GpuUiFontMetrics.textWidth(string, 9.5f * f4);
        builder.text(f8 + f9 - f10 - f20, f7 + f10 + 3.0f * f4, 9.5f * f4, uiHost.muted2Color(), string);
        float f21 = f7 + f10 + 18.0f * f4 + 12.0f * f4;
        for (int i2 = 0; i2 < list2.size(); ++i2) {
            builder.text(f8 + f10, f21 + (float)i2 * f16, f15, uiHost.mutedColor(), list2.get(i2));
        }
        float f22 = f7 + f18 - f10 - f17;
        float f23 = UiKit.buttonWidth(OnboardingTour.xb("\u5a14\ua4ac\u4411\u9962\u107f\u2a9c\u6533\uaf97\u40c9\u7123", 1965257018, 0x16AA02A4 ^ CryptoVault.mask()), f4);
        UiKit.button(builder, uiHost, OnboardingTour.xb("\u5961\udd26\uf293\ub8ac\ua3bf\u50ee\u23ad\u1629\u7c15\u999b\u567e\ub859\u18d3", 2106189718, 0x16AA02A4 ^ CryptoVault.mask()), f8 + f10, f22, f23, f17, f4, UiKit.Style.GHOST, true, f5, f6, this::finish);
        boolean bl2 = this.step == list.size() - 1;
        String string2 = bl2 ? OnboardingTour.xb("\u38ef\u04f7\u154b\u9b24\u4f1f\uadae\u47dc\uf48f\u9567", 273245316, 0x16AA02A4 ^ CryptoVault.mask()) : OnboardingTour.xb("\u3fc3\uc9d3\u9387\uff8f\ue99d\u6b66\u077d", -361062947, 0x16AA02A4 ^ CryptoVault.mask());
        float f24 = UiKit.buttonWidth(string2, f4) + (float)Math.round(10.0f * f4);
        float f25 = UiKit.buttonWidth(OnboardingTour.xb("\u40fd\u74f5\u2517\u4af9\uaeec", -1367916741, 0x16AA02A4 ^ CryptoVault.mask()), f4);
        UiKit.button(builder, uiHost, string2, f8 + f9 - f10 - f24, f22, f24, f17, f4, UiKit.Style.PRIMARY, true, f5, f6, this::next);
        UiKit.button(builder, uiHost, OnboardingTour.xb("\ue336\u1659\uda0a\u94db\u4e3d\u9869\ud150", 1222081749, 0x16AA02A4 ^ CryptoVault.mask()), f8 + f9 - f10 - f24 - (float)Math.round(8.0f * f4) - f25, f22, f25, f17, f4, UiKit.Style.SECONDARY, this.step > 0, f5, f6, this::back);
    }

    private static /* synthetic */ String xb(String string, int n2, int n3) {
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

    @Environment(value=EnvType.CLIENT)
    public record Step(String title, String body, Target target) {
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Target {
        NONE,
        RAIL,
        CATALOG,
        NAV_SETTINGS,
        NAV_CONFIGS,
        NAV_PEOPLE,
        NAV_HUD,
        NAV_SELF_DESTRUCT,
        RAIL_MACROS,
        RAIL_SCRIPTS,
        BAR_UTILS,
        BAR_CONFIGS,
        BAR_MACROS,
        BAR_SCRIPTS,
        BAR_PEOPLE,
        BAR_COSMETICS,
        BAR_BINDS,
        BAR_SELF_DESTRUCT,
        BAR_SS_PROOF,
        BAR_LAYOUT,
        TAB_CLICKGUI,
        TAB_HUD,
        PANEL,
        PANEL_CHEVRON,
        MODULE_ROW,
        MODULE_CHEVRON,
        TOOLSTRIP;


        private static /* synthetic */ String cu(String string, int n2, int n3) {
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
}

