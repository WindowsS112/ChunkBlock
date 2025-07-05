# 🌐 ChunkBlock Plugin

A modern chunk-based island/land-claim plugin inspired by SuperiorSkyblock and ASkyBlock, but designed for land-based gameplay using Minecraft chunks.

---

## 🔧 Features

### 1. Core System
- ✅ Chunk-based private areas (no void islands)
- ✅ Each player/team starts with a main chunk
- ✅ World Management:
  - Custom world support (e.g., `chunk_world`)
  - Void or Overworld generation options
  - Optional claim radius limitations
- ✅ Persistent storage:
  - Supports SQLite, MySQL, or H2
- ✅ Developer-friendly API for easy extension

---

### 2. Chunk Claiming & Management

#### 📦 Claim System
- Claim adjacent chunks
- Expand claim radius through upgrades or leveling
- Automatic protection for claimed chunks
- Set a maximum number of claims per player/team

#### 🔐 Chunk Protection
- Restrict block breaking/placing for outsiders
- Control player/mob damage
- PvP toggle per chunk
- Interaction toggle (doors, levers, buttons)

#### 👥 Access Management
- Add/remove players to claimed chunks
- Role system: Owner, Co-Owner, Member
- Public/private chunk status
- Whitelist/blacklist players from chunks

#### 🔨 Chunk Reset
- Reset a chunk to default state

#### 🗺️ Chunk Visualization
- GUI-based chunk map
- *(Optional)* Dynmap / BlueMap integration for chunk visuals

---

### 3. Economy & Upgrades

#### 💰 Chunk Upgrades
- Expand claim radius
- Faster crop growth
- Boosted mob spawn rates
- Increased ore/resource generation
- Chunk-only power-ups

#### 🛡️ Protection Upgrades
- Temporarily disable mob spawning, PvP, etc.

#### 🏷️ Shop System *(Optional)*
- Buy chunk expansions
- Purchase resources & boosters
- GUI-based shop interface

#### 📈 Leveling System
- Gain levels through placed blocks, kills, and quests
- Leaderboards & rankings
- Unlock special rewards, cosmetics, and expansions

---

### 4. Quests & Challenges

#### 📜 Chunk Quests
- Gather specific blocks in your chunks
- Kill mobs within your chunks
- Complete chunk-specific events

#### 🎁 Rewards
- Money
- Extra claim slots
- Special items & cosmetics

---

### 5. Team System *(Optional)*

#### 👥 Teams
- Create and manage teams
- Roles: Owner, Admin, Member
- Team chat support
- Invite, kick, and promote team members
- Transfer chunks between players or teams

---

### 6. Mob & Entity Management

#### 🧟 Spawn Control
- Enable/disable mob spawning per chunk
- Separate passive and hostile mob controls
- Increase spawn rates with upgrades

#### 🛑 Entity Interaction Restrictions
- Protect armor stands, item frames, and animals
- Prevent unauthorized interaction

---

### 7. GUIs & Commands

#### 📂 GUI Menus
- Chunk claiming and management
- Upgrades & boosters
- Access permissions

#### 🖥️ Core Commands
```bash
/chunkblock create           # Create your first chunk
/chunkblock claim            # Claim adjacent chunk
/chunkblock unclaim          # Unclaim a chunk
/chunkblock tp               # Teleport to your main chunk
/chunkblock invite <player>  # Invite players to your team
/chunkblock kick <player>    # Kick a team member
/chunkblock upgrade          # Purchase chunk upgrades
/chunkblock level            # Check your chunk level
/chunkblock top              # View leaderboard
/chunkblock settings         # Manage chunk options
