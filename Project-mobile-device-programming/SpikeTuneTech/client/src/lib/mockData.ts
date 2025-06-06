import type { Track, Playlist } from "@/types/music";

// Mock data for demonstration - in real app this would come from API
export const mockTracks: Track[] = [
  {
    id: 1,
    title: "Midnight Dreams",
    artist: "Alex Rivera",
    album: "Neon Nights",
    duration: 225, // 3:45
    coverUrl: "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&h=200",
    audioUrl: "", // Would be actual audio URL
    genre: "Electronic"
  },
  {
    id: 2,
    title: "Electric Soul",
    artist: "Luna Star",
    album: "Cosmic Vibes",
    duration: 252, // 4:12
    coverUrl: "https://images.unsplash.com/photo-1571330735066-03aaa9429d89?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&h=200",
    audioUrl: "",
    genre: "Pop"
  },
  {
    id: 3,
    title: "Summer Vibes",
    artist: "Tropical House Crew",
    album: "Beach Sessions",
    duration: 208, // 3:28
    coverUrl: "https://images.unsplash.com/photo-1415201364774-f6f0bb35f28f?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&h=200",
    audioUrl: "",
    genre: "Electronic"
  },
  {
    id: 4,
    title: "Bass Drop",
    artist: "DJ Thunder",
    album: "Club Hits",
    duration: 245, // 4:05
    coverUrl: "https://images.unsplash.com/photo-1519389950473-47ba0277781c?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&h=200",
    audioUrl: "",
    genre: "Electronic"
  },
  {
    id: 5,
    title: "Smooth Jazz",
    artist: "Jazz Ensemble",
    album: "Evening Classics",
    duration: 287, // 4:47
    coverUrl: "https://images.unsplash.com/photo-1461784180009-21121b2f204c?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&h=200",
    audioUrl: "",
    genre: "Jazz"
  }
];

export const mockPlaylists: Playlist[] = [
  {
    id: 1,
    name: "Liked Songs",
    description: "Your favorite tracks",
    coverUrl: "",
    tracks: mockTracks.slice(0, 3),
    isPublic: false
  },
  {
    id: 2,
    name: "Chill Vibes",
    description: "Perfect for relaxing",
    coverUrl: "https://images.unsplash.com/photo-1415201364774-f6f0bb35f28f?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&h=200",
    tracks: [mockTracks[0], mockTracks[4]],
    isPublic: true
  },
  {
    id: 3,
    name: "Workout Mix",
    description: "High energy tracks",
    coverUrl: "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&h=200",
    tracks: [mockTracks[1], mockTracks[3]],
    isPublic: true
  }
];

export const formatTime = (seconds: number): string => {
  const mins = Math.floor(seconds / 60);
  const secs = Math.floor(seconds % 60);
  return `${mins}:${secs.toString().padStart(2, '0')}`;
};
