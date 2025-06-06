import { useState } from 'react';
import { Search, List, MoreHorizontal } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { mockPlaylists } from '@/lib/mockData';
import { useAudio } from '@/hooks/useAudio';

const filterOptions = ['Playlists', 'Artists', 'Albums'];

export default function Library() {
  const [activeFilter, setActiveFilter] = useState('Playlists');
  const { playTrack } = useAudio();

  const handlePlayPlaylist = (playlist: any) => {
    if (playlist.tracks.length > 0) {
      playTrack(playlist.tracks[0], playlist.tracks, 0);
    }
  };

  return (
    <div className="ml-60 pb-24 min-h-screen p-6">
      <div className="flex items-center justify-between mb-8">
        <h1 className="text-3xl font-bold">Your Library</h1>
        <div className="flex items-center gap-4">
          <Button variant="ghost" size="sm" className="text-spotify-text hover:text-white">
            <Search className="w-4 h-4" />
          </Button>
          <Button variant="ghost" size="sm" className="text-spotify-text hover:text-white">
            <List className="w-4 h-4" />
          </Button>
        </div>
      </div>

      <div className="flex gap-4 mb-6">
        {filterOptions.map((option) => (
          <Button
            key={option}
            variant={activeFilter === option ? "default" : "ghost"}
            size="sm"
            onClick={() => setActiveFilter(option)}
            className={
              activeFilter === option 
                ? "bg-spotify-light-gray text-white" 
                : "bg-spotify-light-gray/30 text-white hover:bg-spotify-text/20"
            }
          >
            {option}
          </Button>
        ))}
      </div>

      <div className="space-y-4">
        {mockPlaylists.map((playlist) => (
          <div 
            key={playlist.id}
            className="flex items-center gap-4 p-3 rounded hover:bg-spotify-light-gray/50 transition-colors cursor-pointer group"
            onClick={() => handlePlayPlaylist(playlist)}
          >
            <img 
              src={playlist.coverUrl || "https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?ixlib=rb-4.0.3&auto=format&fit=crop&w=60&h=60"} 
              alt={playlist.name} 
              className="w-12 h-12 rounded"
            />
            <div className="flex-1">
              <h3 className="font-semibold">{playlist.name}</h3>
              <p className="text-spotify-text text-sm">{playlist.tracks.length} songs</p>
            </div>
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
