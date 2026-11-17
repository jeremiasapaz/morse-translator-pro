# Brand Guide — Morse Translator Pro

## Logo / icon
- Primary mark: a Morse glyph `. — .` (dot, dash, dot) on a blue gradient
  rounded square. Source: `icon.svg`.
- Keep the amber dash as the focal accent. Maintain clear space ≥ 10% of the
  icon size on all sides. Don't recolor the glyph except the approved palette.

## Color palette
| Role | Hex | Usage |
|------|-----|-------|
| Primary Blue | `#1565C0` | Brand, primary buttons |
| Deep Blue | `#0D47A1` | Gradients, icon background |
| Signal Amber | `#FFC107` | Accent, "signal on", dashes |
| Light Blue | `#BBDEFB` | Subtitles on dark bg |
| Dark Slate | `#37474F` | "signal off", neutral text |
| White | `#FFFFFF` | Surfaces, glyph highlight |

## Typography
- Marketing: Segoe UI / Roboto / Arial (system sans), bold for headlines.
- In-app: Material 3 default (Roboto) with a large-text accessibility variant.

## Voice & tone
- Clear, friendly, a little playful (occasional dots/dashes in copy).
- Privacy-forward and honest. Never overpromise accuracy of live decoding;
  always mention it depends on conditions.

## Taglines
- Primary: "Translate · Transmit · Decode — Offline."
- Alt: "The modern Morse code toolkit."

## Asset inventory (this folder)
- `icon.svg` — 512px app icon source (export 512×512 PNG for Play).
- `feature-graphic.svg` — 1024×500 Play feature graphic source.
- See `../screenshots/README.md` for screenshot specs.

## Exporting PNGs
Use any SVG→PNG tool (Inkscape, resvg, or a browser). Example with Inkscape:
```
inkscape icon.svg --export-type=png --export-width=512 -o icon-512.png
inkscape feature-graphic.svg --export-type=png --export-width=1024 -o feature-1024x500.png
```
Google Play requires: 512×512 32-bit PNG icon, 1024×500 feature graphic, and
at least 2 phone screenshots (see screenshots/README.md).
