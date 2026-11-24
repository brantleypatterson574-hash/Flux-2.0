package com.yourname.utilmod.module.impl;

import com.yourname.utilmod.module.Category;
import com.yourname.utilmod.module.Module;
import com.yourname.utilmod.setting.BooleanSetting;
import com.yourname.utilmod.setting.StringSetting;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Hides incoming chat lines that contain any of the listed words. The check is
 * client-side only: the packet is never changed or forwarded differently.
 */
public class ChatFilterModule extends Module {
    public static ChatFilterModule INSTANCE;

    private final StringSetting hiddenWords = new StringSetting("Hidden Words", "buy, sell, free rank")
        .description("Comma-separated words or phrases to hide. Empty means nothing is filtered.");
    private final BooleanSetting ignoreCase = new BooleanSetting("Ignore Case", true)
        .description("Match words whether they are typed in capital letters or not.");
    private final BooleanSetting hideSystemMessages = new BooleanSetting("Hide System Messages", false)
        .description("Also hide server announcements that have no player name attached.");

    private final List<String> terms = new ArrayList<>();
    private String cachedSource;
    private boolean cachedIgnoreCase;

    public ChatFilterModule() {
        super("Chat Filter", "Hides chat lines containing your own list of words. Only the text shown to you is affected.",
            Category.UTIL);
        INSTANCE = this;
        addSetting(hiddenWords);
        addSetting(ignoreCase);
        addSetting(hideSystemMessages);
    }

    /** Returns true when a player chat line should be hidden from this client. */
    public static boolean shouldHide(Text text) {
        ChatFilterModule module = INSTANCE;
        if (module == null || !module.isEnabled()) return false;
        module.refresh();
        return module.containsTerm(text);
    }

    /** Returns true when a nameless server message should be hidden from this client. */
    public static boolean shouldHideSystem(Text text) {
        ChatFilterModule module = INSTANCE;
        if (module == null || !module.isEnabled() || !module.hideSystemMessages.isEnabled()) return false;
        module.refresh();
        return module.containsTerm(text);
    }

    private boolean containsTerm(Text text) {
        if (text == null || terms.isEmpty()) return false;
        String message = text.getString();
        if (!cachedIgnoreCase) message = message.toLowerCase(Locale.ROOT);
        for (String term : terms) {
            if (message.contains(term)) return true;
        }
        return false;
    }

    /** Refreshes the parsed term list only when the editable text actually changes. */
    public void refresh() {
        String source = hiddenWords.getValue();
        boolean caseFlag = ignoreCase.isEnabled();
        if (source.equals(cachedSource) && caseFlag == cachedIgnoreCase) return;
        cachedSource = source;
        cachedIgnoreCase = caseFlag;
        terms.clear();
        for (String part : source.split(",")) {
            String term = part.trim();
            if (term.isEmpty()) continue;
            terms.add(caseFlag ? term.toLowerCase(Locale.ROOT) : term);
        }
    }

    @Override
    public void onEnable() {
        cachedSource = null;
        refresh();
    }
}
