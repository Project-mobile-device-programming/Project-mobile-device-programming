import { Heart, Play, Download, MoreHorizontal } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { mockTracks, formatTime } from '@/lib/mockData';
import { useAudio } from '@/hooks/useAudio';

export default function Favorites() {
  const { playTrack } = useAudio();

  // Mock liked songs - first 3 tracks
  const likedSongs = mockTracks.slice(0, 3);

  const handlePlayAll = () => {
    if (likedSongs.length > 0) {
      playTrack(likedSongs[0], likedSongs, 0);
    }
  };

  const handlePlayTrack = (track: any) => {
    const trackIndex = likedSongs.findIndex(t => t.id === track.id);
    playTrack(track, likedSongs, trackIndex);
  };

  return (
    <div className="ml-60 pb-24 min-h-screen p-6">
      <div className="flex items-center gap-6 mb-8">
        <div className="w-48 h-48 bg-gradient-to-br from-purple-500 to-blue-500 rounded-lg flex items-center justify-center">
          <Heart className="w-24 h-24 text-white" />
        </div>
        <div>
          <p className="text-sm font-semibold mb-2">PLAYLIST</p>
          <h1 className="text-5xl font-bold mb-4">Liked Songs</h1>
          <p className="text-spotify-text">User • {likedSongs.length} songs</p>
        </div>
      </div>

      <div className="flex items-center gap-6 mb-6">
        <Button 
          onClick={handlePlayAll}
          className="w-14 h-14 bg-spotify-green rounded-full hover:bg-spotify-green-light shadow-lg hover:scale-105"
        >
          <Play className="w-6 h-6 ml-1" />
        </Button>
        <Button variant="ghost" size="lg" className="text-spotify-text hover:text-white">
          <Heart className="w-6 h-6" />
        </Button>
        <Button variant="ghost" size="lg" className="text-spotify-text hover:text-white">
          <Download className="w-6 h-6" />
        </Button>
        <Button variant="ghost" size="lg" className="text-spotify-text hover:text-white">
          <MoreHorizontal className="w-6 h-6" />
        </Button>
      </div>

      <div className="space-y-2">
        {likedSongs.map((track, index) => (
          <div 
            key={track.id}
            className="flex items-center gap-4 p-3 rounded hover:bg-spotify-light-gray/50 transition-colors group cursor-pointer"
            onClick={() => handlePlayTrack(track)}
          >
            <span className="text-spotify-text w-4">{index + 1}</span>
            <img src={track.coverUrl} alt={track.title} className="w-10 h-10 rounded" />
            <div className="flex-1">
              <h3 className="font-semibold">{track.title}</h3>
              <p className="text-spotify-text text-sm">{track.artist}</p>
            </div>
            <p className="text-spotify-text text-sm">{track.album}</p>
            <Button
              variant="ghost"
              size="sm"
              className="text-spotify-text hover:text-white opacity-0 group-hover:opacity-100 transition-opacity"
            >
              <Heart className="w-4 h-4" />
            </Button>
            <span className="text-spotify-text text-sm w-12">{formatTime(track.duration)}</span>
            <Button
              variant="ghost"
              size="sm"
              className="text-spotify-text hover:text-white opacity-0 group-hover:opacity-100 transition-opacity"
            >
              <MoreHorizontal className="w-4 h-4" />
            </Button>
          </div>
        ))}
      </div>
    </div>
  );
}
