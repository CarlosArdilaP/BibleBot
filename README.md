# BibleBot

A server-side Fabric mod for Minecraft that periodically broadcasts Catholic Bible verses in chat.

## Features

- Broadcasts a random verse at a configurable interval
- Supports multiple languages (`es`, `en`) — easily extensible
- Trinity-related words highlighted in gold via inline `<gold>` tags in the verse files
- OP commands to pause, resume, and reload the mod without restarting the server
- Spanish verses from the **Biblia de Jerusalén**; English verses from the **Douay-Rheims Bible**

## Requirements

- Minecraft **26.1.2**
- [Fabric Loader](https://fabricmc.net/) `>=0.19.2`
- [Fabric API](https://modrinth.com/mod/fabric-api) `0.147.0+26.1.2`

## Installation

1. Download the latest JAR from [Releases](../../releases).
2. Place it in your server's `mods/` folder alongside Fabric API.
3. Start the server — a default config file is created automatically at `config/biblebot.json`.

## Configuration

`config/biblebot.json`:

```json
{
  "language": "es",
  "interval_minutes": 5
}
```

| Field              | Values       | Default | Description                          |
|--------------------|--------------|---------|--------------------------------------|
| `language`         | `es`, `en`   | `es`    | Language for verses and messages     |
| `interval_minutes` | integer >= 1 | `5`     | How often a verse is broadcast       |

After editing the file, use `/bible reload` to apply changes without restarting.

## Commands

All commands require **operator** level (gamemaster or higher).

| Command         | Description                                              |
|-----------------|----------------------------------------------------------|
| `/bible pause`  | Stops broadcasting verses                                |
| `/bible resume` | Resumes broadcasting verses                              |
| `/bible reload` | Reloads `biblebot.json` and the verse/message files      |

## Adding a New Language

1. Create `src/main/resources/assets/biblebot/lang/<code>/` (e.g. `pt/`).
2. Add `messages.json` with all message keys translated (use `es/messages.json` as reference).
3. Add `verses.json` with an array of verse objects. Use `<gold>word</gold>` tags to highlight divine names.

```json
[
  {
    "book": "John",
    "chapter": 1,
    "verse": 1,
    "text": "In the beginning was the <gold>Word</gold>, and the <gold>Word</gold> was with <gold>God</gold>."
  }
]
```

4. Set `"language": "<code>"` in `biblebot.json` and run `/bible reload`.

## Building from Source

```bash
./gradlew build
```

The output JAR is at `build/libs/biblebot-{version}.jar`.

## License

[MIT](LICENSE) © 2026 Carlos Ardila Patiño
