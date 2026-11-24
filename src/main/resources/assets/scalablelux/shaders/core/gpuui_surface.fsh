#version 330

in vec2 v_texCoord;
out vec4 fragColor;

layout(std140) uniform UiParameters {
    vec4 params[16];
};

// Physically motivated window surfaces. One shader, one mode per surface, so every option shares the
// same lighting model: a soft key light up-left that the cursor can take over, a polished top edge,
// a vignette that keeps the panes legible and a dither that stops 8-bit banding in dark gradients.
//
// params[0]: framebuffer width, height, elapsed seconds, frame delta.
// params[1]: mode, opacity, detail (0..1), drift speed (0..2).
// params[2]: cursor UV (outside 0..1 means no pointer), cursor light (0..1), depth toggle (0/1).
// params[3]: tint rgb (the theme accent, used sparingly), grain strength (0..1).

const int FROSTED = 0;
const int SMOKED = 1;
const int OBSIDIAN = 2;
const int CARBON = 3;
const int TITANIUM = 4;
const int MARQUINA = 5;
const int AURORA = 6;
const int DEEPSEA = 7;
const int NEBULA = 8;
const int MESH = 9;
const int MAGMA = 10;
const int GLACIER = 11;
const int CIRCUIT = 12;
const int PRISM = 13;
const int PLASMA = 14;
const int EMBER = 15;
const int RAIN = 16;
const int GALAXY = 17;
const int BIOLUME = 18;
const int SILK = 19;
const int QUARTZ = 20;
const int DUNES = 21;
const int RIFT = 22;
const int CHROME = 23;

float hash12(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

vec2 hash22(vec2 p) {
    vec3 p3 = fract(vec3(p.xyx) * vec3(0.1031, 0.1030, 0.0973));
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.xx + p3.yz) * p3.zy);
}

float vnoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * f * (f * (f * 6.0 - 15.0) + 10.0);
    return mix(mix(hash12(i), hash12(i + vec2(1.0, 0.0)), u.x),
               mix(hash12(i + vec2(0.0, 1.0)), hash12(i + vec2(1.0, 1.0)), u.x), u.y);
}

const mat2 ROT = mat2(0.80, 0.60, -0.60, 0.80);

float fbm(vec2 p) {
    float f = 0.0;
    float a = 0.5;
    for (int i = 0; i < 5; i++) {
        f += a * vnoise(p);
        p = ROT * p * 2.03 + 11.7;
        a *= 0.5;
    }
    return f / 0.96875;
}

// Ridged noise: 1 on thin crests, falling away quickly. Mineral veins, fractures and light threads.
float ridge(vec2 p) {
    float f = 0.0;
    float a = 0.5;
    for (int i = 0; i < 5; i++) {
        f += a * (1.0 - abs(vnoise(p) * 2.0 - 1.0));
        p = ROT * p * 2.07 + 5.3;
        a *= 0.5;
    }
    return f / 0.96875;
}

// Distance to the nearest of a jittered grid of points, and to the second nearest.
vec2 voronoi(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    float d1 = 8.0;
    float d2 = 8.0;
    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {
            vec2 g = vec2(float(x), float(y));
            vec2 o = hash22(i + g);
            vec2 r = g + o - f;
            float d = dot(r, r);
            if (d < d1) { d2 = d1; d1 = d; }
            else if (d < d2) { d2 = d; }
        }
    }
    return sqrt(vec2(d1, d2));
}

const float LOOP = 7200.0;

float lk(float k) {
    return floor(k * LOOP / 6.2831853 + 0.5) * 6.2831853 / LOOP;
}

float lr(float r) {
    return floor(r * LOOP + 0.5) / LOOP;
}

float tr(float k) {
    float w = max(lk(k), 6.2831853 / LOOP);
    return k / w * sin(w * params[0].z);
}

float sat(float x) { return clamp(x, 0.0, 1.0); }

void main() {
    vec2 uv = v_texCoord;
    float aspect = max(0.25, params[0].x / max(params[0].y, 1.0));
    float time = params[0].z;
    int mode = int(params[1].x + 0.5);
    float opacity = clamp(params[1].y, 0.0, 1.0);
    float detail = clamp(params[1].z, 0.0, 1.0);
    vec3 accent = params[3].rgb;
    float grainAmount = clamp(params[3].w, 0.0, 1.0);

    // Aspect-corrected working space, centred so features grow from the middle of the window.
    vec2 p = (uv - 0.5) * vec2(aspect, 1.0);

    // Key light: fixed up-left, handed to the pointer when it is over the window.
    vec2 cursor = params[2].xy;
    float inside = step(0.0, cursor.x) * step(cursor.x, 1.0) * step(0.0, cursor.y) * step(cursor.y, 1.0);
    float follow = clamp(params[2].z, 0.0, 1.0) * inside;
    vec2 lightUv = mix(vec2(0.20, -0.10), cursor, follow);
    vec2 toLight = (uv - lightUv) * vec2(aspect, 1.0);
    float lightDist = length(toLight);
    float key = exp(-lightDist * lightDist * 3.2);
    float wide = exp(-lightDist * 1.35);

    // Parallax: the surface drifts a hair against the pointer, which is what sells depth.
    float depth = clamp(params[2].w, 0.0, 1.0) * inside;
    vec2 parallax = (cursor - 0.5) * 0.016 * depth;
    vec2 q = p - parallax;

    vec3 col = vec3(0.0);
    float alpha = 1.0;

    if (mode == FROSTED) {
        // Etched glass: a cool grey body that lets the blurred scene through, a coarse frost cloud and a
        // fine tooth that catches the light. No pattern, which is what makes it read as a material.
        float frost = fbm(q * 5.5 + 3.1);
        float tooth = hash12(floor(gl_FragCoord.xy) + 17.0);
        float body = 0.125 + 0.022 * frost;
        col = vec3(body * 0.96, body * 1.0, body * 1.08);
        col += vec3(0.85, 0.92, 1.0) * (tooth - 0.5) * 0.018 * (0.5 + detail);
        col += vec3(0.80, 0.88, 1.0) * key * 0.115;
        // Sheen: one broad diagonal band of reflected sky, softened at both ends.
        float diag = dot(uv - vec2(0.5), normalize(vec2(1.0, -0.62)));
        float band = exp(-pow((diag - 0.12) * 4.2, 2.0)) * 0.060 + exp(-pow((diag + 0.34) * 9.0, 2.0)) * 0.025;
        col += vec3(0.88, 0.94, 1.0) * band;
        alpha = 0.46 + 0.16 * frost;
    } else if (mode == SMOKED) {
        // Tinted automotive glass: dense at the bottom, thinning toward the light, two clean reflections.
        float grade = mix(0.78, 0.40, smoothstep(1.0, 0.0, uv.y));
        float haze = fbm(q * 2.4 + vec2(0.0, tr(0.012)));
        float body = 0.030 + 0.020 * haze;
        col = vec3(body * 0.95, body * 1.02, body * 1.12);
        col += vec3(0.62, 0.74, 0.92) * wide * 0.050;
        float d1 = dot(uv - vec2(0.5), normalize(vec2(1.0, -0.78)));
        float refl = exp(-pow((d1 - 0.20) * 7.5, 2.0)) * 0.085 + exp(-pow((d1 - 0.30) * 18.0, 2.0)) * 0.040;
        col += vec3(0.72, 0.82, 1.0) * refl * (1.0 - 0.5 * uv.y);
        alpha = grade * 0.92 + 0.06;
    } else if (mode == OBSIDIAN) {
        // Polished black glass. Nothing inside it; everything you see is the room reflected in the
        // surface: two soft boxes whose position follows the light, a falloff toward the edges and a
        // trace of micro-scratching that only shows where a reflection crosses it.
        vec2 w = q * 1.4;
        float body = fbm(w * 1.2 + 3.0);
        col = vec3(0.012, 0.013, 0.017) + vec3(0.008, 0.009, 0.013) * body;
        // One broad sweep of reflected sky and one thin strip-light, both with long soft shoulders.
        float along = dot((uv - lightUv) * vec2(aspect, 1.0), normalize(vec2(0.82, -0.57)));
        float sweep = smoothstep(-0.58, -0.02, along) * (1.0 - smoothstep(0.02, 0.62, along));
        float strip = exp(-pow((along - 0.66) * 11.0, 2.0));
        float fall = 1.0 - 0.45 * smoothstep(0.1, 1.0, uv.y);
        float scratch = pow(vnoise(vec2(dot(uv * vec2(aspect, 1.0), vec2(0.8, 0.6)) * 240.0, dot(uv, vec2(-0.6, 0.8)) * 5.0)), 4.0);
        float refl = (sweep * 0.115 + strip * 0.075) * fall;
        col += vec3(0.58, 0.66, 0.86) * refl * (0.80 + 0.20 * body);
        col += vec3(0.70, 0.78, 0.95) * scratch * refl * 0.22 * (0.3 + detail);
        col += vec3(0.50, 0.56, 0.72) * wide * 0.040;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == CARBON) {
        // 2x2 twill under clearcoat. Each strand is a cylinder lit along its own axis, so the weave
        // flips between bright and dark as the light moves, which is the whole carbon-fibre look.
        float cells = 44.0 + 22.0 * (1.0 - detail);
        vec2 g = uv * vec2(aspect, 1.0) * cells + parallax * cells;
        vec2 id = floor(g);
        vec2 f = fract(g);
        float over = mod(id.x + id.y, 4.0) < 2.0 ? 1.0 : 0.0;
        float dirH = over;
        float across = dirH > 0.5 ? f.y : f.x;
        float along = dirH > 0.5 ? f.x : f.y;
        float roundness = sqrt(max(0.0, 1.0 - pow(across * 2.0 - 1.0, 2.0)));
        vec2 axis = dirH > 0.5 ? vec2(1.0, 0.0) : vec2(0.0, 1.0);
        vec2 ldir = normalize(toLight + vec2(0.0001));
        float aniso = pow(sat(1.0 - abs(dot(ldir, axis))), 1.6);
        float spec = aniso * roundness * (0.35 + 0.65 * key + 0.25 * wide);
        float fibre = hash12(vec2(id.x * 0.37 + floor(f.x * 6.0), id.y * 0.91 + floor(f.y * 6.0)));
        float shade = 0.016 + 0.040 * roundness + 0.010 * fibre;
        shade *= 0.55 + 0.45 * sin((along + id.x * 0.5 + id.y * 0.5) * 3.1416);
        col = vec3(shade) * vec3(0.95, 1.0, 1.08);
        col += vec3(0.72, 0.80, 0.95) * spec * 0.095;
        // Gaps between strands.
        float gap = smoothstep(0.0, 0.10, across) * smoothstep(1.0, 0.90, across);
        col *= 0.35 + 0.65 * gap;
        // Clearcoat reflection over the weave.
        float d = dot(uv - vec2(0.5), normalize(vec2(1.0, -0.7)));
        col += vec3(0.78, 0.86, 1.0) * exp(-pow((d - 0.16) * 5.0, 2.0)) * 0.040;
        alpha = 0.96 * opacity + 0.04;
    } else if (mode == TITANIUM) {
        // Bead-blasted, linear-brushed titanium: streaks stretched along x, one anisotropic band of
        // light across the middle that slides with the cursor, warm-cool split in the highlight.
        float streak = vnoise(vec2(uv.x * 3.0 * aspect + parallax.x * 6.0, uv.y * 520.0));
        float streak2 = vnoise(vec2(uv.x * 11.0 * aspect, uv.y * 190.0 + 7.0));
        float grainy = mix(streak, streak2, 0.45) - 0.5;
        float bandCentre = mix(0.30, lightUv.y, follow);
        float band = exp(-pow((uv.y - bandCentre) * 3.1, 2.0));
        float across = exp(-pow((uv.x - mix(0.5, lightUv.x, follow)) * 1.4 / aspect * 1.2, 2.0));
        float base = 0.060 + 0.020 * (1.0 - uv.y);
        float lum = base + grainy * (0.030 + 0.035 * detail) + band * across * 0.085 + band * 0.025;
        col = vec3(lum) * vec3(1.02, 1.00, 0.97);
        col += vec3(0.95, 0.88, 0.78) * pow(band, 3.0) * across * 0.040;
        col += vec3(0.78, 0.86, 1.0) * key * 0.030;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == MARQUINA) {
        // Nero Marquina: polished black marble with a few long, sharp white veins and a pale halo.
        vec2 m = q * 2.6;
        vec2 warp = vec2(fbm(m * 0.7 + 1.9), fbm(m * 0.7 + 8.4));
        vec2 mw = m + (warp - 0.5) * 2.4;
        float cloud = fbm(mw * 0.8 + 3.0);
        float r = ridge(mw * 0.85);
        float vein = pow(r, 15.0);
        float halo = pow(r, 4.0);
        float fine = pow(ridge(mw * 2.1 + 17.0), 18.0) * smoothstep(0.45, 0.8, fbm(mw * 1.3 + 4.0));
        col = vec3(0.016, 0.017, 0.020) + vec3(0.020, 0.021, 0.025) * cloud;
        col += vec3(0.60, 0.63, 0.68) * halo * 0.060 * (0.5 + detail);
        col += vec3(0.92, 0.93, 0.96) * vein * (0.34 + 0.26 * detail);
        col += vec3(0.80, 0.82, 0.88) * fine * 0.20;
        // Polish: a broad soft reflection, not a spot.
        col += vec3(0.60, 0.66, 0.78) * pow(sat(1.0 - lightDist * 0.85), 2.4) * 0.065;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == AURORA) {
        // Curtains of ionised oxygen folded across a clear night sky: each ribbon has a bright lower
        // hem that undulates, striations that run up from it, a green core and a violet crown.
        vec3 sky = mix(vec3(0.004, 0.008, 0.020), vec3(0.014, 0.030, 0.056), pow(uv.y, 1.3));
        float lean = p.x * 1.35 + (0.5 - uv.y) * 0.34;
        float slow = tr(0.018);
        float fold = fbm(vec2(lean * 0.9 + slow, 3.1));
        float hem = 0.70 + 0.17 * (fold - 0.5) * 2.0 + 0.05 * sin(lean * 3.1 + time * lk(0.09));
        float presence = smoothstep(0.34, 0.66, fbm(vec2(lean * 1.15 - slow * 0.6, 8.4)));
        float below = smoothstep(hem + 0.045, hem - 0.015, uv.y);
        float height = exp(-max(hem - uv.y, 0.0) * 3.4);
        float stria = 0.50 + 0.50 * vnoise(vec2(lean * 46.0 + fold * 6.0, uv.y * 2.0 + tr(0.05)));
        float stria2 = 0.60 + 0.40 * vnoise(vec2(lean * 19.0 - fold * 4.0, 1.3));
        float inten = presence * below * height * mix(0.62, 1.0, stria * stria2);
        vec3 green = vec3(0.10, 0.88, 0.46);
        vec3 teal = vec3(0.06, 0.58, 0.74);
        vec3 violet = vec3(0.46, 0.20, 0.82);
        float h = clamp((hem - uv.y) / 0.55, 0.0, 1.0);
        vec3 aur = mix(green, teal, smoothstep(0.0, 0.5, h));
        aur = mix(aur, violet, smoothstep(0.45, 1.0, h));
        aur = mix(aur, accent * 1.5, 0.10);
        col = sky + aur * inten * (0.46 + 0.26 * detail);
        col += aur * 0.020 * presence * smoothstep(0.0, hem, uv.y);
        vec2 sg = uv * vec2(aspect, 1.0) * 70.0;
        vec2 sc = floor(sg);
        float star = step(0.9965, hash12(sc)) * smoothstep(0.35, 0.0, length(fract(sg) - 0.5));
        col += vec3(0.8, 0.88, 1.0) * star * 0.5 * (1.0 - clamp(inten * 2.2, 0.0, 1.0)) * (0.65 + 0.35 * sin(time * lk(1.3) + hash12(sc) * 40.0));
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == DEEPSEA) {
        // Looking up through clear water: a green-blue column darkening with depth, an interference
        // caustic on the surface above (light focused by moving swell), shafts and drifting silt.
        float depthT = uv.y;
        vec3 water = mix(vec3(0.020, 0.160, 0.200), vec3(0.002, 0.022, 0.046), pow(depthT, 0.75));
        // The interference pattern is periodic, so a slow swell warps the lookup and breaks the repeat.
        vec2 swell = vec2(fbm(q * 1.3 + vec2(tr(0.02), 4.0)), fbm(q * 1.3 + vec2(9.0, -tr(0.017))));
        vec2 pp = q * 2.7 * 6.2831853 + (swell - 0.5) * 11.0 - 250.0;
        vec2 i = pp;
        float c = 1.0;
        const float inten = 0.0055;
        for (int n = 0; n < 4; n++) {
            float t2 = time * lk(0.45 * (1.0 - 3.2 / float(n + 1)));
            i = pp + vec2(cos(t2 - i.x) + sin(t2 + i.y), sin(t2 - i.y) + cos(t2 + i.x));
            float sx = sin(i.x + t2);
            float cy = cos(i.y + t2);
            float ax = pp.x * inten * cy;
            float ay = pp.y * inten * sx;
            c += abs(sx * cy) / max(sqrt(ax * ax + ay * ay), 1.0e-4);
        }
        c /= 4.0;
        c = 1.17 - pow(max(c, 0.0), 1.4);
        float caustic = pow(abs(c), 8.0);
        caustic = clamp(caustic, 0.0, 1.0) * pow(1.0 - depthT, 1.15);
        float shafts = fbm(vec2(p.x * 2.6 + p.y * 1.7 + tr(0.02), 2.0));
        shafts = smoothstep(0.42, 0.85, shafts) * pow(1.0 - depthT, 1.2);
        col = water + vec3(0.42, 0.95, 0.95) * caustic * (0.22 + 0.22 * detail) + vec3(0.16, 0.50, 0.55) * shafts * 0.15;
        float silt = step(0.992, hash12(floor(uv * vec2(aspect, 1.0) * 90.0 + vec2(0.0, tr(0.3)))));
        col += vec3(0.5, 0.8, 0.85) * silt * 0.06;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == NEBULA) {
        // Emission nebula: layered dust lanes in front of glowing gas, two temperature zones, stars.
        vec2 n = q * 1.9;
        vec2 warp = vec2(fbm(n + vec2(tr(0.006), 0.0)), fbm(n + vec2(6.1, 2.2) - vec2(0.0, tr(0.005))));
        float gas = fbm(n * 1.1 + 2.4 * warp);
        float dust = fbm(n * 1.9 + 3.0 * warp + 12.0);
        float shape = smoothstep(0.30, 0.78, gas) * (1.0 - 0.75 * smoothstep(0.50, 0.82, dust));
        vec3 hot = mix(vec3(0.85, 0.20, 0.35), accent * 1.4, 0.45);
        vec3 cool = vec3(0.14, 0.30, 0.78);
        vec3 gasCol = mix(cool, hot, smoothstep(0.35, 0.80, fbm(n * 0.7 + 21.0)));
        col = vec3(0.006, 0.007, 0.016) + gasCol * shape * (0.20 + 0.18 * detail);
        vec2 sg = uv * vec2(aspect, 1.0) * 85.0;
        vec2 cell = floor(sg);
        float h = hash12(cell);
        float star = step(0.985, h) * smoothstep(0.42, 0.0, length(fract(sg) - 0.5)) * (0.4 + 0.6 * hash12(cell + 3.0));
        col += mix(vec3(0.7, 0.8, 1.0), vec3(1.0, 0.85, 0.7), hash12(cell + 8.0)) * star * 0.8;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == MAGMA) {
        // Molten basalt: a near-black crust fractured by ridged veins of glowing magma, slowly surfacing.
        vec2 m = q * 2.2;
        vec2 drift = vec2(0.0, -tr(0.05));
        vec2 warp = vec2(fbm(m * 0.8 + drift + 1.4), fbm(m * 0.8 + drift + 6.7));
        vec2 mw = m + (warp - 0.5) * 2.0;
        float crust = fbm(mw * 0.9 + 2.0);
        float r = ridge(mw * 1.1 + drift * 0.6);
        float vein = pow(r, 6.0);
        float glow = pow(r, 2.5);
        vec3 rock = vec3(0.020, 0.012, 0.010) + vec3(0.016, 0.010, 0.009) * crust;
        vec3 hot = mix(vec3(1.0, 0.25, 0.02), vec3(1.0, 0.75, 0.15), vein);
        col = rock;
        col += hot * glow * (0.30 + 0.30 * detail);
        col += hot * vein * 0.85;
        col += vec3(1.0, 0.55, 0.20) * key * 0.05;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == GLACIER) {
        // Faceted glacial ice: voronoi cells read as cracked facets, each catching the light differently.
        vec2 g = q * (5.0 + 3.0 * detail) + parallax * 2.0;
        vec2 vr = voronoi(g);
        float facet = smoothstep(0.0, 0.08, vr.y - vr.x);
        float crackLine = 1.0 - smoothstep(0.0, 0.035, vr.y - vr.x);
        float cellShade = hash12(floor(g));
        vec3 ice = mix(vec3(0.70, 0.85, 0.95), vec3(0.86, 0.94, 1.0), cellShade);
        col = ice * (0.10 + 0.05 * cellShade);
        col += vec3(0.75, 0.90, 1.0) * key * 0.16 * (0.5 + 0.5 * cellShade);
        col += vec3(0.95, 0.98, 1.0) * crackLine * 0.14;
        col *= 0.6 + 0.4 * facet;
        col += vec3(0.80, 0.90, 1.0) * wide * 0.030;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == CIRCUIT) {
        // Dark circuit board: an etched grid of traces pulsing with the accent colour, nodes glinting at junctions.
        float cell = 9.0 + 6.0 * detail;
        vec2 g = q * cell;
        vec2 cellId = floor(g);
        vec2 f = fract(g);
        float h = hash12(cellId);
        float laneX = smoothstep(0.46, 0.50, f.x) * (1.0 - smoothstep(0.50, 0.54, f.x));
        float laneY = smoothstep(0.46, 0.50, f.y) * (1.0 - smoothstep(0.50, 0.54, f.y));
        float lane = max(laneX * step(0.5, h), laneY * step(0.5, fract(h * 7.0)));
        float pulse = 0.5 + 0.5 * sin(time * lk(1.4) - (cellId.x + cellId.y) * 0.9 + h * 6.2831);
        float node = smoothstep(0.10, 0.0, length(f - 0.5)) * step(0.82, h);
        col = vec3(0.012, 0.013, 0.015);
        col += accent * lane * (0.10 + 0.30 * pulse);
        col += accent * node * (0.45 + 0.35 * pulse);
        col += vec3(1.0) * node * 0.12;
        col += accent * key * 0.03;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == PRISM) {
        // Holographic film: a slow iridescent sheen sweeping across dark glass, colour keyed to view angle.
        float sweep = p.x * 1.1 + p.y * 0.6 + tr(0.05);
        float hue = fract(sweep * 0.5 + fbm(q * 1.8 + tr(0.02)) * 0.4);
        vec3 rainbow = vec3(
            0.5 + 0.5 * cos(6.2831 * (hue + 0.00)),
            0.5 + 0.5 * cos(6.2831 * (hue + 0.33)),
            0.5 + 0.5 * cos(6.2831 * (hue + 0.67)));
        float sheen = pow(sat(1.0 - abs(fract(sweep) - 0.5) * 2.0), 3.0);
        float grain = fbm(q * 6.0 + 9.0) - 0.5;
        col = vec3(0.012, 0.012, 0.016) + rainbow * sheen * (0.22 + 0.18 * detail);
        col += rainbow * 0.03 * (0.5 + 0.5 * grain);
        col = mix(col, col + accent * 0.08, key);
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == PLASMA) {
        // Swirling electric plasma: layered sine fields warped by flow noise, a hot core along the ridges.
        vec2 pp = q * 2.0;
        float pt = tr(0.35);
        float swirl = fbm(pp * 1.3 + vec2(pt * 0.4, -pt * 0.3));
        vec2 w = pp + (swirl - 0.5) * 1.6;
        float wave = sin(w.x * 3.1 + time * lk(0.56)) + sin(w.y * 2.6 - time * lk(0.455)) + sin((w.x + w.y) * 1.9 + time * lk(0.735));
        float inten = sat(wave * 0.25 + 0.5);
        vec3 coreA = mix(vec3(0.05, 0.02, 0.10), accent, 0.55);
        vec3 coreB = mix(accent, vec3(1.0, 0.95, 0.85), 0.5);
        col = mix(coreA, coreB, pow(inten, 2.2)) * (0.35 + 0.35 * detail);
        col += vec3(1.0) * pow(inten, 8.0) * 0.35;
        col += accent * key * 0.10;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == EMBER) {
        // Charred ash drifting over a dim glow, with rising embers that fade as they climb.
        vec3 ash = vec3(0.012, 0.009, 0.008) + vec3(0.03, 0.012, 0.006) * fbm(q * 1.6 + vec2(0.0, -tr(0.03)));
        float emberGlow = pow(sat(1.0 - uv.y), 2.5);
        col = ash + vec3(0.6, 0.18, 0.04) * emberGlow * 0.12;
        float sparks = 0.0;
        for (int i = 0; i < 3; i++) {
            float fl = float(i);
            float speed = 0.07 + fl * 0.05;
            vec2 cellUv = uv * vec2(aspect, 1.0) * (14.0 + fl * 8.0);
            cellUv.y += tr(speed * 14.0);
            vec2 cell = floor(cellUv);
            vec2 f = fract(cellUv);
            float h = hash12(cell + fl * 71.0);
            if (h > 0.90) {
                vec2 wobble = vec2(sin(time * lk(2.0) + h * 40.0) * 0.08, 0.0);
                float d = length(f - vec2(0.5) - wobble);
                float life = fract(time * lr(speed * 0.5) + h);
                float fade = sat(1.0 - life) * sat(life * 6.0);
                sparks += (1.0 - smoothstep(0.0, 0.12, d)) * fade * (1.0 - fl * 0.25);
            }
        }
        col += vec3(1.0, 0.55, 0.18) * sparks * 0.9;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == RAIN) {
        // Rain-streaked glass over soft, drifting city-light bokeh.
        float bok = fbm(p * 1.1 + 4.0);
        vec3 night = mix(vec3(0.010, 0.012, 0.018), vec3(0.05, 0.04, 0.03), smoothstep(0.4, 0.9, bok));
        vec2 bg = uv * vec2(aspect, 1.0) * 6.0;
        vec2 bgc = floor(bg);
        float bh = hash12(bgc);
        float boke = step(0.90, bh) * smoothstep(0.5, 0.0, length(fract(bg) - 0.5))
                * (0.5 + 0.5 * sin(time * lk(0.7) + bh * 20.0));
        vec3 lightCol = mix(vec3(1.0, 0.75, 0.45), vec3(0.55, 0.75, 1.0), hash12(bgc + 5.0));
        col = night + lightCol * boke * 0.22;
        vec2 streakUv = vec2(uv.x * aspect * 26.0, uv.y * 4.0 - tr(1.1));
        float lane = hash12(floor(vec2(streakUv.x, 0.0)));
        float drip = fract(streakUv.y + lane * 9.0);
        float streak = smoothstep(0.0, 0.5, drip) * (1.0 - smoothstep(0.5, 1.0, drip)) * step(0.72, lane);
        col += vec3(0.75, 0.85, 1.0) * streak * 0.16;
        col += vec3(0.6, 0.7, 0.85) * key * 0.03;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == GALAXY) {
        // A spiral galaxy seen face-on: a bright core, dust-laned spiral arms, scattered stars.
        vec2 c = q * 1.6;
        float rad = length(c);
        float ang = atan(c.y, c.x) + time * lk(0.04) * 0.5;
        float spiral = sin(ang * 2.0 + rad * 6.0 - time * lk(0.15));
        float arms = smoothstep(0.1, 0.9, 0.5 + 0.5 * spiral) * exp(-rad * 1.1);
        float core = exp(-rad * rad * 9.0);
        vec3 armCol = mix(vec3(0.20, 0.30, 0.75), accent, 0.4);
        col = vec3(0.004, 0.004, 0.010);
        col += armCol * arms * (0.35 + 0.25 * detail);
        col += mix(vec3(1.0, 0.95, 0.8), accent, 0.3) * core * 0.9;
        vec2 sg = uv * vec2(aspect, 1.0) * 95.0;
        vec2 cell = floor(sg);
        float h = hash12(cell);
        float star = step(0.986, h) * smoothstep(0.4, 0.0, length(fract(sg) - 0.5)) * (0.5 + 0.5 * sin(time * lk(1.1) + h * 30.0));
        col += vec3(0.85, 0.9, 1.0) * star * 0.7;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == BIOLUME) {
        // Bioluminescent reef cells pulsing in the dark, each cell on its own slow rhythm.
        vec2 g = q * 3.4;
        vec2 vr = voronoi(g);
        vec2 cellId = floor(g);
        float h = hash12(cellId);
        float pulse = 0.5 + 0.5 * sin(time * lk(0.6 + h * 0.8) + h * 20.0);
        float cellGlow = exp(-vr.x * 9.0) * pulse;
        vec3 hue = mix(vec3(0.10, 0.85, 0.65), vec3(0.65, 0.20, 0.85), hash12(cellId + 3.0));
        col = vec3(0.004, 0.010, 0.012);
        col += hue * cellGlow * (0.6 + 0.4 * detail);
        col += hue * exp(-vr.x * 2.5) * 0.06;
        col += vec3(0.3, 0.6, 0.7) * wide * 0.02;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == SILK) {
        // Flowing silk: soft folds catching a slow diagonal sheen.
        vec2 w = q * 2.2;
        float fold = fbm(w * 1.1 + vec2(tr(0.03), tr(0.02)));
        float ridgeFold = 0.5 + 0.5 * sin(fold * 10.0 + w.x * 1.5);
        vec3 cloth = mix(vec3(0.05, 0.045, 0.06), vec3(0.11, 0.10, 0.125), ridgeFold);
        float diag = dot(uv - 0.5, normalize(vec2(1.0, -0.5) + vec2(0.0, 0.05 * sin(time * lk(0.1)))));
        float sheen = exp(-pow((diag - 0.05) * 3.0, 2.0)) * 0.22 + exp(-pow((diag + 0.35) * 7.0, 2.0)) * 0.10;
        col = cloth;
        col += mix(cloth, accent, 0.4) * sheen * (0.7 + 0.3 * ridgeFold);
        col += vec3(0.9, 0.9, 0.95) * key * 0.05;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == QUARTZ) {
        // Translucent crystal quartz: a faceted interior with light refracting along ridged seams.
        vec2 m = q * 2.0;
        vec2 vr = voronoi(m * 1.4);
        float facet = sat((vr.y - vr.x) * 3.0);
        float seam = 1.0 - smoothstep(0.0, 0.05, vr.y - vr.x);
        float inner = fbm(m * 1.8 + 7.0);
        vec3 crystal = mix(vec3(0.10, 0.06, 0.14), vec3(0.20, 0.14, 0.26), inner);
        col = crystal * (0.5 + 0.5 * facet);
        col += vec3(0.85, 0.70, 1.0) * seam * 0.30;
        col += mix(vec3(0.7, 0.6, 0.95), accent, 0.5) * key * 0.18;
        col += vec3(0.9, 0.85, 1.0) * wide * 0.04;
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == DUNES) {
        // Windswept dunes: slow-drifting ripple bands under a warm, hazy light.
        vec2 w = q * 2.4 + vec2(tr(0.015), 0.0);
        float ripple = sin((w.x + fbm(w * 0.6) * 1.4) * 14.0) * 0.5 + 0.5;
        float macro = fbm(w * 0.5 + 3.0);
        vec3 sand = mix(vec3(0.10, 0.07, 0.045), vec3(0.16, 0.12, 0.08), macro);
        col = sand * (0.7 + 0.3 * ripple);
        col += vec3(0.9, 0.6, 0.35) * key * 0.10;
        col += vec3(0.8, 0.5, 0.3) * pow(sat(1.0 - lightDist * 0.9), 3.0) * 0.08;
        float haze = fbm(w * 1.8 + tr(0.03));
        col = mix(col, vec3(0.18, 0.13, 0.10), haze * 0.08);
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == RIFT) {
        // A fractured void rift: dark space split by a glowing crack, light bleeding from the seam.
        vec2 w = q * 1.6;
        float crackPath = w.x + 0.25 * sin(w.y * 2.0 + time * lk(0.3)) + 0.5 * (fbm(w * 1.3 + time * 0.02) - 0.5);
        float seam = exp(-abs(crackPath) * 14.0);
        float wideSeam = exp(-abs(crackPath) * 4.0);
        col = vec3(0.004, 0.003, 0.008) + vec3(0.01, 0.006, 0.02) * fbm(w * 0.8 + 9.0);
        vec3 riftCol = mix(accent, vec3(1.0, 0.9, 1.0), 0.4);
        col += riftCol * wideSeam * 0.20;
        col += vec3(1.0) * seam * 0.75;
        vec2 sg = uv * vec2(aspect, 1.0) * 70.0;
        vec2 cell = floor(sg);
        float star = step(0.99, hash12(cell)) * smoothstep(0.4, 0.0, length(fract(sg) - 0.5));
        col += vec3(0.8, 0.85, 1.0) * star * 0.4 * (1.0 - sat(wideSeam * 2.0));
        alpha = 0.97 * opacity + 0.03;
    } else if (mode == CHROME) {
        // Polished liquid chrome: a mirror-smooth surface with bright reflection bands drifting across it.
        float base = 0.11 + 0.02 * fbm(q * 3.0 + 1.0);
        col = vec3(base) * vec3(1.0, 1.02, 1.05);
        for (int i = 0; i < 3; i++) {
            float fi = float(i);
            float bandPos = fract(time * lr(0.05 + fi * 0.02) + fi * 0.33);
            float bandD = abs(fract(uv.x - bandPos + 0.5) - 0.5);
            float band = exp(-pow(bandD * (6.0 - fi * 1.2), 2.0));
            col += vec3(0.9, 0.93, 1.0) * band * (0.20 - fi * 0.04);
        }
        col += vec3(1.0) * key * 0.22;
        col += vec3(0.8, 0.85, 0.95) * wide * 0.05;
        alpha = 0.97 * opacity + 0.03;
    } else {
        // Mesh gradient: four soft blobs in accent-derived hues over graphite, with film grain. The
        // current standard for premium dark interfaces.
        float ta = time * lk(0.05);
        vec2 b1 = vec2(0.18 + 0.05 * sin(ta), 0.22 + 0.06 * cos(time * lk(0.065)));
        vec2 b2 = vec2(0.82 + 0.05 * cos(time * lk(0.045)), 0.30 + 0.06 * sin(ta));
        vec2 b3 = vec2(0.58 + 0.06 * sin(time * lk(0.055)), 0.92 + 0.04 * cos(ta));
        vec2 b4 = vec2(0.30 + 0.05 * cos(time * lk(0.035)), 0.85);
        vec3 hueA = mix(vec3(0.22, 0.30, 0.95), accent, 0.35);
        vec3 hueB = mix(vec3(0.62, 0.20, 0.80), accent, 0.20);
        vec3 hueC = mix(vec3(0.05, 0.62, 0.74), accent, 0.40);
        vec3 hueD = mix(vec3(0.90, 0.35, 0.40), accent, 0.30);
        vec2 s = vec2(aspect, 1.0);
        float w1 = exp(-dot((uv - b1) * s, (uv - b1) * s) * 3.4);
        float w2 = exp(-dot((uv - b2) * s, (uv - b2) * s) * 3.6);
        float w3 = exp(-dot((uv - b3) * s, (uv - b3) * s) * 3.9);
        float w4 = exp(-dot((uv - b4) * s, (uv - b4) * s) * 5.0);
        col = vec3(0.030, 0.031, 0.038) + hueA * w1 * 0.50 + hueB * w2 * 0.42 + hueC * w3 * 0.46 + hueD * w4 * 0.30;
        alpha = 0.97 * opacity + 0.03;
    }

    // Vignette: corners fall away so the panes keep their contrast.
    vec2 vv = (uv - 0.5) * vec2(1.0, 1.15);
    float vignette = 1.0 - 0.32 * smoothstep(0.30, 0.95, length(vv) * 1.35);
    col *= vignette;

    // Polished top edge: a one-pixel-ish lit bevel for every surface.
    float bevel = smoothstep(0.012, 0.0, uv.y) * 0.07;
    col += vec3(0.85, 0.90, 1.0) * bevel * (0.6 + 0.4 * key);

    // Dither: breaks up banding in the dark gradients; strength follows the Grain setting.
    float n = hash12(gl_FragCoord.xy + fract(params[0].z * 7.13) * 91.7) + hash12(gl_FragCoord.xy * 1.37 + 5.0) - 1.0;
    col += n * (1.0 / 255.0) * (1.0 + 5.0 * grainAmount);

    fragColor = vec4(max(col, 0.0), clamp(alpha, 0.0, 1.0));
}
