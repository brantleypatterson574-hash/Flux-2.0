package com.yourname.utilmod.ui;

import com.yourname.utilmod.module.Category;

/**
 * Dropdown menu / HUD palettes. These are original recolourings inspired by the
 * general look of popular clients - no proprietary assets or copied code.
 */
public enum GuiTheme {
    MATRIX(
        "Matrix",
        0x9907080A, 0xFF101013, 0xFFFF3B3B, 0xFFFF3B3B,
        0xFF141417, 0xFF2C2C33, 0xFF1A1A1F, 0xFF101013,
        0xFF1A1A1E, 0xFF2A1417, 0xFF25252B, 0xFF17171B, 0xFF212127,
        0xFFECECF0, 0xFF8B8B95, 0xFFFF3B3B, 0xFF4B4B54,
        0xFF0D0D10, 0xFF0E0E11, 0xFF34343C, 0xFF383840,
        0xAA000000, true, false
    ),
    UTILMOD(
        "UtilMod",
        0x8F060608, 0xFFE51617, 0xFF38BDF8, 0xFFE51617,
        0xFF1C1C20, 0xFF3F3F46, 0xFF26262C, 0xFF1E2A33,
        0xFF232327, 0xFF23323B, 0xFF3F3F46, 0xFF161619, 0xFF222228,
        0xFFE4E4E7, 0xFFA1A1AA, 0xFF22C55E, 0xFFEF4444,
        0xFF0A0A0A, 0xFF18181B, 0xFF52525B, 0xFF3F3F46,
        0x88000000, false, false
    ),
    PRESTIGE(
        // Config-stable identifier; the palette itself is the Ochard 1.21.11 theme.
        "Orchard",
        0x991C1E24, 0xFF1C1E24, 0xFFCF00FF, 0xFF78B4FF,
        0xE60F1117, 0x38B5ADCA, 0xFF1C1E24, 0xFF0F1117,
        0xF0090A10, 0x2ACF00FF, 0x2A78B4FF, 0xF0090A10, 0xFF1C1E24,
        0xFFFDF5FF, 0xFFB9BCC2, 0xFF50DC78, 0xFF4A4C5A,
        0xFF2A2D36, 0xFF0F1117, 0xFF78B4FF, 0xFF2A2D36, 0x99000000, true, false
    ),
    FLUX(
        "Flux",
        // Shipped ScalableLux FLUX palette. Enum name and ordinal are unchanged
        // so saved configs keep resolving to this theme.
        0x99040404, 0xFF0D0D0D, 0xFFE4E4E7, 0xFFB8B8BE,
        0xFF101010, 0x61B8B8BE, 0xFF050505, 0xFF1A1A1A,
        0xFF141414, 0x38B8B8BE, 0xFF202024, 0xFF141414, 0x66B8B8BE,
        0xFFF4F4F5, 0xFF9A9AA0, 0xFFB8B8BE, 0xFF333338,
        0xFF050505, 0xFF101010, 0x61B8B8BE, 0xFF6E6E73,
        0x99000000, true, false
    ),
    METEOR(
        "Meteor",
        0x8F0B0D10, 0xFF16191D, 0xFF3E8CFF, 0xFF3E8CFF,
        0xFF1B1E22, 0xFF010409, 0xFF24282D, 0xFF16304A,
        0xFF22262B, 0xFF1E2B38, 0xFF30363D, 0xFF191C20, 0xFF24282D,
        0xFFE6EDF3, 0xFF8B949E, 0xFF3FB950, 0xFFF85149,
        0xFF0D1117, 0xFF0D1117, 0xFF30363D, 0xFF30363D,
        0x88000000, false, false
    ),
    LUMI(
        "Lumi",
        0x66433B6B, 0xFFF7F5FF, 0xFF7C5CE0, 0xFF7C5CE0,
        0xFFF8F7FD, 0xFFD8D2EA, 0xFFEDE9FA, 0xFFE2D9FF,
        0xFFFFFFFF, 0xFFEDE7FF, 0xFFE4DCFA, 0xFFF0ECFC, 0xFFF0ECFC,
        0xFF241E3A, 0xFF6F6890, 0xFF12A594, 0xFFCFC6E8,
        0xFFE6E0F5, 0xFFFFFFFF, 0xFFCFC6E8, 0xFFD8D2EA,
        0x33000000, true, true
    ),
    SAKURA(
        "Sakura",
        0x8A1A121C, 0xFF1B151B, 0xFFFFC6D6, 0xFFFFB3C8,
        0xFF261C28, 0xFF5A3A50, 0xFF382739, 0xFF482D43,
        0xFF2C2130, 0xFF473245, 0xFF5A3E54, 0xFF2C202B, 0xFF2C202B,
        0xFFFFF5F9, 0xFFD8B9C7, 0xFFFFB0C9, 0xFF6B5361,
        0xFF1C151E, 0xFF161119, 0xFF5A4253, 0xFF5A4253,
        0x99000000, true, false
    ),
    OCEAN(
        "Ocean",
        0x8F0A1822, 0xFF0F1A24, 0xFFBAE6FD, 0xFF7DD3FC,
        0xFF162633, 0xFF2E5670, 0xFF1D3748, 0xFF23465B,
        0xFF1A2E3D, 0xFF244659, 0xFF315F7E, 0xFF1B3040, 0xFF1B3040,
        0xFFF0FAFF, 0xFFB4CDDC, 0xFF8EE1FF, 0xFF527286,
        0xFF11212D, 0xFF0C1822, 0xFF355B74, 0xFF355B74,
        0x99000000, true, false
    ),
    SLATE(
        "Slate",
        0x8A141618, 0xFF1B1D20, 0xFFDDE1E6, 0xFFBFC6CF,
        0xFF202328, 0xFF3A3F47, 0xFF292D33, 0xFF31363E,
        0xFF24272C, 0xFF343A42, 0xFF414851, 0xFF252930, 0xFF252930,
        0xFFEEF1F4, 0xFFA5ACB6, 0xFFD2D8DF, 0xFF5A616B,
        0xFF1A1D21, 0xFF121417, 0xFF484F58, 0xFF484F58,
        0x99000000, true, false
    ),
    // ScalableLux 27-theme palette port. Appended so existing enum names/config stay stable.
    SL_LIQUID_GLASS(
        "Liquid Glass",
        0x90050607, 0xFF0B0D0F, 0xFFEAFAF6, 0xFF2EE6C8,
        0xFF0B0D0F, 0x38FFFFFF, 0xFF050607, 0xFFFFFFFF,
        0xFF0B0D0F, 0x38FFFFFF, 0x38FFFFFF, 0xFF0B0D0F, 0x38FFFFFF,
        0xFFEFF2F3, 0xFFB4BABE, 0xFF2EE6C8, 0xFF25282C,
        0xFF050607, 0xFF0B0D0F, 0x38FFFFFF, 0xFF1FB8A0, 0x99000000, true, false
    ),
    SL_FLUX(
        "Flux (SL)",
        0x99040404, 0xFF0D0D0D, 0xFFE4E4E7, 0xFFB8B8BE,
        0xFF101010, 0x61B8B8BE, 0xFF050505, 0xFF1A1A1A,
        0xFF141414, 0x38B8B8BE, 0xFF202024, 0xFF141414, 0x66B8B8BE,
        0xFFF4F4F5, 0xFF9A9AA0, 0xFFB8B8BE, 0xFF333338,
        0xFF050505, 0xFF101010, 0x61B8B8BE, 0xFF6E6E73, 0x99000000, true, false
    ),
    SL_GRANITE(
        "Granite",
        0x90050505, 0xFF0E0C0A, 0xFFF6E3B4, 0xFFD9B56E,
        0xFF0E0C0A, 0x66D9B56E, 0xFF050505, 0xFFF5E6C8,
        0xFF0E0C0A, 0x66D9B56E, 0x66D9B56E, 0xFF0E0C0A, 0x66D9B56E,
        0xFFF6F2E9, 0xFFADA79B, 0xFFD9B56E, 0xFF2C2A26,
        0xFF050505, 0xFF0E0C0A, 0x66D9B56E, 0xFFA6823E, 0x99000000, true, false
    ),
    SL_NERO_PORTORO(
        "Nero Portoro",
        0x90030304, 0xFF0A0A0C, 0xFFF3E6C0, 0xFFDBB872,
        0xFF0A0A0C, 0x58DBB872, 0xFF030304, 0xFFE8DCC0,
        0xFF0A0A0C, 0x58DBB872, 0x58DBB872, 0xFF0A0A0C, 0x58DBB872,
        0xFFF1EFE9, 0xFFA7A39E, 0xFFDBB872, 0xFF2A2927,
        0xFF030304, 0xFF0A0A0C, 0x58DBB872, 0xFFA6823E, 0x99000000, true, false
    ),
    SL_TEAL(
        "Teal",
        0x90020708, 0xFF061416, 0xFF8FF5E6, 0xFF2DD4BF,
        0xFF061416, 0x662DD4BF, 0xFF020708, 0xFFBFF7EE,
        0xFF061416, 0x662DD4BF, 0x662DD4BF, 0xFF061416, 0x662DD4BF,
        0xFFEAFAF7, 0xFF93B8B3, 0xFF2DD4BF, 0xFF1C2B2E,
        0xFF020708, 0xFF061416, 0x662DD4BF, 0xFF1496A8, 0x99000000, true, false
    ),
    SL_RUBY(
        "Ruby",
        0x90080204, 0xFF18060B, 0xFFFF9DB0, 0xFFF0486B,
        0xFF18060B, 0x66F0486B, 0xFF080204, 0xFFFFC8D2,
        0xFF18060B, 0x66F0486B, 0x66F0486B, 0xFF18060B, 0x66F0486B,
        0xFFFDECEF, 0xFFBC9EA5, 0xFFF0486B, 0xFF2F1D22,
        0xFF080204, 0xFF18060B, 0x66F0486B, 0xFFA8143E, 0x99000000, true, false
    ),
    SL_CATPPUCCIN(
        "Catppuccin",
        0x9011111B, 0xFF1E1E2E, 0xFFF5C2E7, 0xFFCBA6F7,
        0xFF1E1E2E, 0x66CBA6F7, 0xFF11111B, 0xFFB4BEFE,
        0xFF1E1E2E, 0x66CBA6F7, 0x66CBA6F7, 0xFF1E1E2E, 0x66CBA6F7,
        0xFFCDD6F4, 0xFFA6ADC8, 0xFFCBA6F7, 0xFF313244,
        0xFF11111B, 0xFF1E1E2E, 0x66CBA6F7, 0xFFB07FE0, 0x99000000, true, false
    ),
    SL_PURPLE(
        "Purple",
        0x9005020A, 0xFF0E071C, 0xFFD8B4FF, 0xFFA86EFF,
        0xFF0E071C, 0x66A86EFF, 0xFF05020A, 0xFFDCC8FF,
        0xFF0E071C, 0x66A86EFF, 0x66A86EFF, 0xFF0E071C, 0x66A86EFF,
        0xFFF3ECFF, 0xFFAD9FCB, 0xFFA86EFF, 0xFF241B38,
        0xFF05020A, 0xFF0E071C, 0x66A86EFF, 0xFF6A35C8, 0x99000000, true, false
    ),
    SL_EMERALD(
        "Emerald",
        0x90020805, 0xFF06140D, 0xFF9BF5C2, 0xFF34D37D,
        0xFF06140D, 0x6634D37D, 0xFF020805, 0xFFC4F5D8,
        0xFF06140D, 0x6634D37D, 0x6634D37D, 0xFF06140D, 0x6634D37D,
        0xFFEBFAF1, 0xFF92BCA4, 0xFF34D37D, 0xFF1B2C22,
        0xFF020805, 0xFF06140D, 0x6634D37D, 0xFF139A6A, 0x99000000, true, false
    ),
    SL_OCEAN(
        "Ocean (SL)",
        0x9002040A, 0xFF070D18, 0xFF9DC0FF, 0xFF3B82F6,
        0xFF070D18, 0x663B82F6, 0xFF02040A, 0xFFC8D8F7,
        0xFF070D18, 0x663B82F6, 0x663B82F6, 0xFF070D18, 0x663B82F6,
        0xFFEAF1FD, 0xFF96AAC8, 0xFF3B82F6, 0xFF1B2636,
        0xFF02040A, 0xFF070D18, 0x663B82F6, 0xFF1F5FD6, 0x99000000, true, false
    ),
    SL_SUNSET(
        "Sunset",
        0x90080402, 0xFF18100A, 0xFFFFC08A, 0xFFFF8C42,
        0xFF18100A, 0x66FF8C42, 0xFF080402, 0xFFFFE0C4,
        0xFF18100A, 0x66FF8C42, 0x66FF8C42, 0xFF18100A, 0x66FF8C42,
        0xFFFDF0E8, 0xFFC8AC98, 0xFFFF8C42, 0xFF2F241B,
        0xFF080402, 0xFF18100A, 0x66FF8C42, 0xFFD6502F, 0x99000000, true, false
    ),
    SL_ROSE(
        "Rose",
        0x90080205, 0xFF18070F, 0xFFFFA6C8, 0xFFFF5FA2,
        0xFF18070F, 0x66FF5FA2, 0xFF080205, 0xFFFFCADB,
        0xFF18070F, 0x66FF5FA2, 0x66FF5FA2, 0xFF18070F, 0x66FF5FA2,
        0xFFFDECF3, 0xFFC79EAF, 0xFFFF5FA2, 0xFF2F1D26,
        0xFF080205, 0xFF18070F, 0x66FF5FA2, 0xFFC81F68, 0x99000000, true, false
    ),
    SL_MIDNIGHT(
        "Midnight",
        0x90020206, 0xFF08090F, 0xFFAEB6FF, 0xFF5B6CFF,
        0xFF08090F, 0x665B6CFF, 0xFF020206, 0xFFC8CCF7,
        0xFF08090F, 0x665B6CFF, 0x665B6CFF, 0xFF08090F, 0x665B6CFF,
        0xFFE9EBFD, 0xFF9EA2C8, 0xFF5B6CFF, 0xFF1C1E36,
        0xFF020206, 0xFF08090F, 0x665B6CFF, 0xFF2F3FD6, 0x99000000, true, false
    ),
    SL_GOLD(
        "Gold",
        0x90070502, 0xFF14110A, 0xFFF3DCA0, 0xFFE3B341,
        0xFF14110A, 0x66E3B341, 0xFF070502, 0xFFF0E2C0,
        0xFF14110A, 0x66E3B341, 0x66E3B341, 0xFF14110A, 0x66E3B341,
        0xFFFAF4E6, 0xFFC0B296, 0xFFE3B341, 0xFF2C261B,
        0xFF070502, 0xFF14110A, 0x66E3B341, 0xFFB8891F, 0x99000000, true, false
    ),
    SL_SLATE(
        "Slate (SL)",
        0x90040506, 0xFF0B0D10, 0xFFC2CDD8, 0xFF8AA0B4,
        0xFF0B0D10, 0x4C8AA0B4, 0xFF040506, 0xFFD0D8E0,
        0xFF0B0D10, 0x4C8AA0B4, 0x4C8AA0B4, 0xFF0B0D10, 0x4C8AA0B4,
        0xFFEEF1F4, 0xFF9EA7B1, 0xFF8AA0B4, 0xFF222831,
        0xFF040506, 0xFF0B0D10, 0x4C8AA0B4, 0xFF5A6B7A, 0x99000000, true, false
    ),
    SL_COPPER(
        "Copper",
        0x90060302, 0xFF130D08, 0xFFE8B78A, 0xFFC77B4A,
        0xFF130D08, 0x66C77B4A, 0xFF060302, 0xFFFFDCC0,
        0xFF130D08, 0x66C77B4A, 0x66C77B4A, 0xFF130D08, 0x66C77B4A,
        0xFFFAF0E8, 0xFFCDAF98, 0xFFC77B4A, 0xFF2C2219,
        0xFF060302, 0xFF130D08, 0x66C77B4A, 0xFF9C5A2E, 0x99000000, true, false
    ),
    SL_AMBER(
        "Amber",
        0x90070402, 0xFF161006, 0xFFFFD270, 0xFFFFB300,
        0xFF161006, 0x66FFB300, 0xFF070402, 0xFFFFE8B8,
        0xFF161006, 0x66FFB300, 0x66FFB300, 0xFF161006, 0x66FFB300,
        0xFFFCF1E0, 0xFFCBB388, 0xFFFFB300, 0xFF2F2410,
        0xFF070402, 0xFF161006, 0x66FFB300, 0xFFB87800, 0x99000000, true, false
    ),
    SL_MONO(
        "Mono",
        0x90040404, 0xFF0C0C0D, 0xFFF5F5F7, 0xFFE4E4E7,
        0xFF0C0C0D, 0x4CE4E4E7, 0xFF040404, 0xFFF0F0F2,
        0xFF0C0C0D, 0x4CE4E4E7, 0x4CE4E4E7, 0xFF0C0C0D, 0x4CE4E4E7,
        0xFFF5F5F6, 0xFFAAAAAE, 0xFFE4E4E7, 0xFF28282B,
        0xFF040404, 0xFF0C0C0D, 0x4CE4E4E7, 0xFFA0A0A6, 0x99000000, true, false
    ),
    SL_INDIGO(
        "Indigo",
        0x90030308, 0xFF0A0B16, 0xFFAEB4FF, 0xFF4F5BD5,
        0xFF0A0B16, 0x664F5BD5, 0xFF030308, 0xFFC8CAF5,
        0xFF0A0B16, 0x664F5BD5, 0x664F5BD5, 0xFF0A0B16, 0x664F5BD5,
        0xFFEBEBFC, 0xFF9EA2CB, 0xFF4F5BD5, 0xFF1E2038,
        0xFF030308, 0xFF0A0B16, 0x664F5BD5, 0xFF2F37A8, 0x99000000, true, false
    ),
    SL_LIME(
        "Lime",
        0x90040702, 0xFF0E1408, 0xFFC8F78F, 0xFFA3E635,
        0xFF0E1408, 0x66A3E635, 0xFF040702, 0xFFDDF5B8,
        0xFF0E1408, 0x66A3E635, 0x66A3E635, 0xFF0E1408, 0x66A3E635,
        0xFFF4FBE6, 0xFFBED198, 0xFFA3E635, 0xFF26301A,
        0xFF040702, 0xFF0E1408, 0x66A3E635, 0xFF6FA312, 0x99000000, true, false
    ),
    SL_CORAL(
        "Coral",
        0x90080303, 0xFF180A08, 0xFFFFA698, 0xFFFF6B5B,
        0xFF180A08, 0x66FF6B5B, 0xFF080303, 0xFFFFC9C0,
        0xFF180A08, 0x66FF6B5B, 0x66FF6B5B, 0xFF180A08, 0x66FF6B5B,
        0xFFFDEDE9, 0xFFCBA29B, 0xFFFF6B5B, 0xFF2F1C19,
        0xFF080303, 0xFF180A08, 0x66FF6B5B, 0xFFD6422E, 0x99000000, true, false
    ),
    SL_LAVENDER(
        "Lavender",
        0x9005040A, 0xFF120E1C, 0xFFDCC8FF, 0xFFC4A3FF,
        0xFF120E1C, 0x66C4A3FF, 0xFF05040A, 0xFFE4D4FF,
        0xFF120E1C, 0x66C4A3FF, 0x66C4A3FF, 0xFF120E1C, 0x66C4A3FF,
        0xFFF6F0FF, 0xFFB9ABD0, 0xFFC4A3FF, 0xFF2A2238,
        0xFF05040A, 0xFF120E1C, 0x66C4A3FF, 0xFF8A5FD6, 0x99000000, true, false
    ),
    SL_CRIMSON(
        "Crimson",
        0x90070202, 0xFF160707, 0xFFFF8A8A, 0xFFDC2626,
        0xFF160707, 0x66DC2626, 0xFF070202, 0xFFFFC4C4,
        0xFF160707, 0x66DC2626, 0x66DC2626, 0xFF160707, 0x66DC2626,
        0xFFFDECEC, 0xFFCB9B9B, 0xFFDC2626, 0xFF2F1818,
        0xFF070202, 0xFF160707, 0x66DC2626, 0xFF961414, 0x99000000, true, false
    ),
    SL_GRAPHITE(
        "Graphite",
        0x90060607, 0xFF101114, 0xFFBCD4FF, 0xFF6EA8FF,
        0xFF101114, 0x586EA8FF, 0xFF060607, 0xFFD4DEF0,
        0xFF101114, 0x586EA8FF, 0x586EA8FF, 0xFF101114, 0x586EA8FF,
        0xFFECEEF2, 0xFFA4A8B0, 0xFF6EA8FF, 0xFF26282D,
        0xFF060607, 0xFF101114, 0x586EA8FF, 0xFF4A78C8, 0x99000000, true, false
    ),
    SL_AURORA_NIGHT(
        "Aurora Night",
        0x9002050A, 0xFF060D14, 0xFFA8FAD6, 0xFF5AF2B0,
        0xFF060D14, 0x665AF2B0, 0xFF02050A, 0xFFC4F8E4,
        0xFF060D14, 0x665AF2B0, 0x665AF2B0, 0xFF060D14, 0x665AF2B0,
        0xFFE9F8F3, 0xFF93B8B4, 0xFF5AF2B0, 0xFF182630,
        0xFF02050A, 0xFF060D14, 0x665AF2B0, 0xFF7A5AD8, 0x99000000, true, false
    ),
    SL_SAKURA(
        "Sakura (SL)",
        0x90070407, 0xFF160D12, 0xFFFFD0E2, 0xFFFFA8C8,
        0xFF160D12, 0x66FFA8C8, 0xFF070407, 0xFFFFE0EC,
        0xFF160D12, 0x66FFA8C8, 0x66FFA8C8, 0xFF160D12, 0x66FFA8C8,
        0xFFFDF0F5, 0xFFC9AEBB, 0xFFFFA8C8, 0xFF2C1D25,
        0xFF070407, 0xFF160D12, 0x66FFA8C8, 0xFFD8689A, 0x99000000, true, false
    ),
    SL_ABYSS(
        "Abyss",
        0x90010204, 0xFF04080E, 0xFFD0EAFF, 0xFFA5D8FF,
        0xFF04080E, 0x58A5D8FF, 0xFF010204, 0xFFD8ECFF,
        0xFF04080E, 0x58A5D8FF, 0x58A5D8FF, 0xFF04080E, 0x58A5D8FF,
        0xFFE8F2FB, 0xFF90A6BA, 0xFFA5D8FF, 0xFF111C28,
        0xFF010204, 0xFF04080E, 0x58A5D8FF, 0xFF4A86C0, 0x99000000, true, false
    ),
    PINK(
        // Appended so every existing enum name/ordinal stays config-stable.
        "Pink",
        0x99151015, 0xFF1F1A1F, 0xFFEE8EEA, 0xFFD96AD8,
        0xE6151015, 0xFF382F37, 0xFF3F343F, 0xFF302A30,
        0xFF1E191E, 0xFF3F343F, 0xFF272127, 0xFF1A1519, 0xFF272127,
        0xFFF4F4F5, 0xFF9E909E, 0xFFD96AD8, 0xFF3A343A,
        0xFF1E191E, 0xFF1E191E, 0xFF382F37, 0xFF382F37, 0x99000000, true, false
     ),
    ;

    private final String label;
    private final int[] baseColors;
    private static boolean cloudy;

    // Mutable so the optional Cloudy pass can soften every palette in place.
    // All menu renderers read these fields, so one toggle updates both layouts
    // and the theme swatches without duplicating palette definitions.
    public int overlay;
    public int titleBar;
    public int titleAccent;
    public int accent;
    public int panel;
    public int panelBorder;
    public int header;
    public int headerGradientEnd;
    public int moduleRow;
    public int moduleRowEnabled;
    public int moduleRowHover;
    public int settingRow;
    public int settingRowHover;
    public int text;
    public int textSecondary;
    public int toggleOn;
    public int toggleOff;
    public int scrollTrack;
    public int searchBg;
    public int searchBorder;
    public int outline;
    public int shadow;
    public final boolean rounded;
    public final boolean light;

    GuiTheme(String label, int overlay, int titleBar, int titleAccent, int accent,
             int panel, int panelBorder, int header, int headerGradientEnd,
             int moduleRow, int moduleRowEnabled, int moduleRowHover,
             int settingRow, int settingRowHover,
             int text, int textSecondary, int toggleOn, int toggleOff,
             int scrollTrack, int searchBg, int searchBorder, int outline,
             int shadow, boolean rounded, boolean light) {
        this.label = label;
        this.baseColors = new int[] {
            overlay, titleBar, titleAccent, accent, panel, panelBorder, header,
            headerGradientEnd, moduleRow, moduleRowEnabled, moduleRowHover,
            settingRow, settingRowHover, text, textSecondary, toggleOn,
            toggleOff, scrollTrack, searchBg, searchBorder, outline, shadow
        };
        this.rounded = rounded;
        this.light = light;
        refresh();
    }

    public String getLabel() {
        return label;
    }

    /** Enum popup labels use the exact palette name, not the stable config identifier. */
    @Override
    public String toString() {
        return label;
    }

    /** Replaces only the alpha byte of an ARGB colour. */
    public static int withAlpha(int color, int alpha) {
        return (alpha << 24) | (color & 0x00FFFFFF);
    }

    public static boolean isCloudy() {
        return cloudy;
    }

    /**
     * Optional Cloudy pass: every shipped palette gains a hazier, lower-contrast
     * version instead of adding separate themes. Alpha is preserved so the panel
     * opacity and overlay settings keep working exactly as before.
     */
    public static void setCloudy(boolean enabled) {
        if (cloudy == enabled) return;
        cloudy = enabled;
        for (GuiTheme theme : values()) theme.refresh();
    }

    private void refresh() {
        int[] colors = cloudy ? soften(baseColors, light) : baseColors;
        overlay = colors[0];
        titleBar = colors[1];
        titleAccent = colors[2];
        accent = colors[3];
        panel = colors[4];
        panelBorder = colors[5];
        header = colors[6];
        headerGradientEnd = colors[7];
        moduleRow = colors[8];
        moduleRowEnabled = colors[9];
        moduleRowHover = colors[10];
        settingRow = colors[11];
        settingRowHover = colors[12];
        text = colors[13];
        textSecondary = colors[14];
        toggleOn = colors[15];
        toggleOff = colors[16];
        scrollTrack = colors[17];
        searchBg = colors[18];
        searchBorder = colors[19];
        outline = colors[20];
        shadow = colors[21];
    }

    private static int[] soften(int[] base, boolean light) {
        int haze = light ? 0xFFDCE8F4 : 0xFF9CB3C8;
        int textTarget = light ? 0xFF263243 : 0xFFFFFFFF;
        int[] out = new int[base.length];
        out[0] = mixed(base[0], haze, light ? 0.18f : 0.16f);  // overlay
        out[1] = mixed(base[1], haze, 0.22f);                  // titleBar
        out[2] = mixed(base[2], haze, light ? 0.18f : 0.28f);  // titleAccent
        out[3] = mixed(base[3], haze, light ? 0.18f : 0.28f);  // accent
        out[4] = mixed(base[4], haze, 0.24f);                  // panel
        out[5] = mixed(base[5], haze, 0.30f);                  // panelBorder
        out[6] = mixed(base[6], haze, 0.24f);                  // header
        out[7] = mixed(base[7], haze, 0.24f);                  // headerGradientEnd
        out[8] = mixed(base[8], haze, 0.22f);                  // moduleRow
        out[9] = mixed(base[9], haze, 0.22f);                  // moduleRowEnabled
        out[10] = mixed(base[10], haze, light ? 0.20f : 0.26f);// moduleRowHover
        out[11] = mixed(base[11], haze, 0.22f);                // settingRow
        out[12] = mixed(base[12], haze, light ? 0.20f : 0.26f);// settingRowHover
        out[13] = mixed(base[13], textTarget, 0.12f);          // text
        out[14] = mixed(base[14], textTarget, light ? 0.18f : 0.14f); // textSecondary
        out[15] = mixed(base[15], haze, 0.24f);                // toggleOn
        out[16] = mixed(base[16], haze, 0.18f);                // toggleOff
        out[17] = mixed(base[17], haze, 0.22f);                // scrollTrack
        out[18] = mixed(base[18], haze, 0.24f);                // searchBg
        out[19] = mixed(base[19], haze, 0.30f);                // searchBorder
        out[20] = mixed(base[20], haze, 0.24f);                // outline
        out[21] = mixed(base[21], haze, 0.24f);                // shadow
        return out;
    }

    private static int mixed(int color, int target, float amount) {
        return (color & 0xFF000000)
            | (component((color >>> 16) & 0xFF, (target >>> 16) & 0xFF, amount) << 16)
            | (component((color >>> 8) & 0xFF, (target >>> 8) & 0xFF, amount) << 8)
            | component(color & 0xFF, target & 0xFF, amount);
    }

    private static int component(int from, int to, float amount) {
        return Math.max(0, Math.min(255, Math.round(from + (to - from) * amount)));
    }

    /**
     * The Ochard palette uses a compact six-colour category ramp. Keep the stable
     * category order while giving the active theme its own stripe colours; every
     * other palette keeps the original per-category colours.
     */
    public int categoryColor(Category category) {
        if (category == null) return accent;
        if (this == PRESTIGE) {
            return switch (category) {
                case CRYSTAL -> 0xFF64C8FF;
                case SWORD -> 0xFFFF6EB4;
                case MACE -> 0xFFFFA028;
                case MOVEMENT -> 0xFFFFDC32;
                case PLAYER -> 0xFF50DC78;
                case RENDER -> 0xFF8C32C8;
                case WORLD -> 0xFF78B4FF;
                case DONUT -> 0xFFCF00FF;
                case UTIL -> 0xFFA0A0A0;
                case HUD -> 0xFFF0ABFC;
            };
        }
        return category.getColor();
    }
}
