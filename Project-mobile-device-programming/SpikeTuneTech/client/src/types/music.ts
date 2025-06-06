export interface Track {
  id: number;
  title: string;
  artist: string;
  album?: string;
  duration: number;
  coverUrl?: string;
  audioUrl?: string;
  genre?: string;
}

export interface Playlist {
  id: number;
  name: string;
  description?: string;
  coverUrl?: string;
  tracks: Track[];
  isPublic: boolean;
}

export interface PlayerState {
  currentTrack: Track | null;
  isPlaying: boolean;
  currentTime: number;
  duration: number;
  volume: number;
  isShuffle: boolean;
  isRepeat: boolean;
  queue: Track[];
  queueIndex: number;
}

export interface User {
  id: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  profileImageUrl?: string;
}
