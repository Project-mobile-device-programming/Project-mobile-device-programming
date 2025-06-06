import {
  users,
  songs,
  playlists,
  playlistSongs,
  userFavorites,
  userDownloads,
  type User,
  type UpsertUser,
  type Song,
  type InsertSong,
  type Playlist,
  type InsertPlaylist,
  type PlaylistSong,
  type InsertPlaylistSong,
  type UserFavorite,
  type InsertUserFavorite,
  type UserDownload,
  type InsertUserDownload,
} from "@shared/schema";
import { db } from "./db";
import { eq, desc, and } from "drizzle-orm";

// Interface for storage operations
export interface IStorage {
  // User operations (IMPORTANT: these are mandatory for Replit Auth)
  getUser(id: string): Promise<User | undefined>;
  upsertUser(user: UpsertUser): Promise<User>;
  
  // Song operations
  getAllSongs(): Promise<Song[]>;
  getSong(id: number): Promise<Song | undefined>;
  createSong(song: InsertSong): Promise<Song>;
  
  // Playlist operations
  getUserPlaylists(userId: string): Promise<Playlist[]>;
  getPlaylist(id: number): Promise<Playlist | undefined>;
  createPlaylist(playlist: InsertPlaylist): Promise<Playlist>;
  getPlaylistSongs(playlistId: number): Promise<(PlaylistSong & { song: Song })[]>;
  addSongToPlaylist(data: InsertPlaylistSong): Promise<PlaylistSong>;
  
  // Favorites operations
  getUserFavorites(userId: string): Promise<(UserFavorite & { song: Song })[]>;
  addFavorite(data: InsertUserFavorite): Promise<UserFavorite>;
  removeFavorite(userId: string, songId: number): Promise<void>;
  isFavorite(userId: string, songId: number): Promise<boolean>;
  
  // Downloads operations
  getUserDownloads(userId: string): Promise<(UserDownload & { song: Song })[]>;
  addDownload(data: InsertUserDownload): Promise<UserDownload>;
  removeDownload(userId: string, songId: number): Promise<void>;
}

export class DatabaseStorage implements IStorage {
  // User operations (IMPORTANT: these are mandatory for Replit Auth)
  async getUser(id: string): Promise<User | undefined> {
    const [user] = await db.select().from(users).where(eq(users.id, id));
    return user;
  }

  async upsertUser(userData: UpsertUser): Promise<User> {
    const [user] = await db
      .insert(users)
      .values(userData)
      .onConflictDoUpdate({
        target: users.id,
        set: {
          ...userData,
          updatedAt: new Date(),
        },
      })
      .returning();
    return user;
  }

  // Song operations
  async getAllSongs(): Promise<Song[]> {
    return db.select().from(songs).orderBy(desc(songs.createdAt));
  }

  async getSong(id: number): Promise<Song | undefined> {
    const [song] = await db.select().from(songs).where(eq(songs.id, id));
    return song;
  }

  async createSong(song: InsertSong): Promise<Song> {
    const [newSong] = await db.insert(songs).values(song).returning();
    return newSong;
  }

  // Playlist operations
  async getUserPlaylists(userId: string): Promise<Playlist[]> {
    return db.select().from(playlists).where(eq(playlists.userId, userId)).orderBy(desc(playlists.createdAt));
  }

  async getPlaylist(id: number): Promise<Playlist | undefined> {
    const [playlist] = await db.select().from(playlists).where(eq(playlists.id, id));
    return playlist;
  }

  async createPlaylist(playlist: InsertPlaylist): Promise<Playlist> {
    const [newPlaylist] = await db.insert(playlists).values(playlist).returning();
    return newPlaylist;
  }

  async getPlaylistSongs(playlistId: number): Promise<(PlaylistSong & { song: Song })[]> {
    const result = await db
      .select({
        id: playlistSongs.id,
        playlistId: playlistSongs.playlistId,
        songId: playlistSongs.songId,
        position: playlistSongs.position,
        addedAt: playlistSongs.addedAt,
        song: songs,
      })
      .from(playlistSongs)
      .innerJoin(songs, eq(playlistSongs.songId, songs.id))
      .where(eq(playlistSongs.playlistId, playlistId))
      .orderBy(playlistSongs.position);
    
    return result;
  }

  async addSongToPlaylist(data: InsertPlaylistSong): Promise<PlaylistSong> {
    const [playlistSong] = await db.insert(playlistSongs).values(data).returning();
    return playlistSong;
  }

  // Favorites operations
  async getUserFavorites(userId: string): Promise<(UserFavorite & { song: Song })[]> {
    const result = await db
      .select({
        id: userFavorites.id,
        userId: userFavorites.userId,
        songId: userFavorites.songId,
        createdAt: userFavorites.createdAt,
        song: songs,
      })
      .from(userFavorites)
      .innerJoin(songs, eq(userFavorites.songId, songs.id))
      .where(eq(userFavorites.userId, userId))
      .orderBy(desc(userFavorites.createdAt));
    
    return result;
  }

  async addFavorite(data: InsertUserFavorite): Promise<UserFavorite> {
    const [favorite] = await db.insert(userFavorites).values(data).returning();
    return favorite;
  }

  async removeFavorite(userId: string, songId: number): Promise<void> {
    await db
      .delete(userFavorites)
      .where(and(eq(userFavorites.userId, userId), eq(userFavorites.songId, songId)));
  }

  async isFavorite(userId: string, songId: number): Promise<boolean> {
    const [favorite] = await db
      .select()
      .from(userFavorites)
      .where(and(eq(userFavorites.userId, userId), eq(userFavorites.songId, songId)));
    return !!favorite;
  }

  // Downloads operations
  async getUserDownloads(userId: string): Promise<(UserDownload & { song: Song })[]> {
    const result = await db
      .select({
        id: userDownloads.id,
        userId: userDownloads.userId,
        songId: userDownloads.songId,
        downloadedAt: userDownloads.downloadedAt,
        song: songs,
      })
      .from(userDownloads)
      .innerJoin(songs, eq(userDownloads.songId, songs.id))
      .where(eq(userDownloads.userId, userId))
      .orderBy(desc(userDownloads.downloadedAt));
    
    return result;
  }

  async addDownload(data: InsertUserDownload): Promise<UserDownload> {
    const [download] = await db.insert(userDownloads).values(data).returning();
    return download;
  }

  async removeDownload(userId: string, songId: number): Promise<void> {
    await db
      .delete(userDownloads)
      .where(and(eq(userDownloads.userId, userId), eq(userDownloads.songId, songId)));
  }
}

export const storage = new DatabaseStorage();
