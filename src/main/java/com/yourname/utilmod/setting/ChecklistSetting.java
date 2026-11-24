package com.yourname.utilmod.setting;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A searchable list of toggleable options for the dropdown menu.
 *
 * <p>Used by ESP so the user can choose exactly which entity types or blocks are
 * highlighted instead of being limited to a few hard-coded categories.</p>
 */
public class ChecklistSetting extends Setting {
    public record Option(String id, String label) {}

    private final String key;
    private final List<Option> options;
    private final Set<String> defaults;
    private final Set<String> selected = new LinkedHashSet<>();
    private final Set<String> readOnlySelected = Collections.unmodifiableSet(selected);

    public ChecklistSetting(String name, String key, Collection<Option> options,
                            Collection<String> defaultIds) {
        super(name);
        this.key = key;
        this.options = List.copyOf(options);
        this.defaults = Set.copyOf(defaultIds);

        if (SelectionStore.has(key)) {
            selected.addAll(SelectionStore.load(key));
        } else {
            selected.addAll(defaults);
        }
    }

    /** Re-applies persisted selections after SelectionStore.reload(). */
    public void reloadFromStore() {
        if (!SelectionStore.has(key)) return;
        selected.clear();
        selected.addAll(SelectionStore.load(key));
    }

    public List<Option> getOptions() {
        return options;
    }

    /** Immutable snapshot, kept for callers that really need one. */
    public Set<String> getSelected() {
        return Set.copyOf(selected);
    }

    /** Read-only live view. Render loops only call contains(), so they need no copy. */
    public Set<String> selectedView() {
        return readOnlySelected;
    }

    public boolean isSelected(String id) {
        return selected.contains(id);
    }

    public void toggle(String id) {
        if (!selected.remove(id)) selected.add(id);
        SelectionStore.save(key, selected);
    }

    public void setSelected(Collection<String> ids) {
        selected.clear();
        if (ids != null) selected.addAll(ids);
        SelectionStore.save(key, selected);
    }

    public void selectAll() {
        for (Option option : options) selected.add(option.id());
        SelectionStore.save(key, selected);
    }

    public void clear() {
        selected.clear();
        SelectionStore.save(key, selected);
    }

    public String getDisplayValue() {
        return selected.isEmpty() ? "None" : selected.size() + " selected";
    }

    @Override
    public void reset() {
        selected.clear();
        selected.addAll(defaults);
        SelectionStore.save(key, selected);
    }

    @Override
    protected String getDefaultDescription() {
        return "Click to open the searchable checklist and choose exactly which entries to include. Right-click resets to defaults.";
    }
}
