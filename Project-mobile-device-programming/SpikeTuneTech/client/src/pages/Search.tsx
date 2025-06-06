import { useState } from 'react';
import { Search as SearchIcon, Music, Mic, Guitar, Headphones } from 'lucide-react';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { mockTracks } from '@/lib/mockData';
import { useAudio } from '@/hooks/useAudio';

const genres = [
  { name: 'Pop', color: 'from-red-500 to-red-600', icon: Music },
  { name: 'Hip-Hop', color: 'from-green-500 to-green-600', icon: Mic },
  { name: 'Rock', color: 'from-blue-500 to-blue-600', icon: Guitar },
  { name: 'Electronic', color: 'from-purple-500 to-purple-600', icon: Headphones },
];

export default function Search() {
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState<any[]>([]);
  const { playTrack } = useAudio();

  const handleSearch = (query: string) => {
    setSearchQuery(query);
    if (query.trim()) {
      // Filter mock tracks based on search query
      const results = mockTracks.filter(track =>
        track.title.toLowerCase().includes(query.toLowerCase()) ||
        track.artist.toLowerCase().includes(query.toLowerCase()) ||
        track.album?.toLowerCase().includes(query.toLowerCase())
      );
      setSearchResults(results);
    } else {
      setSearchResults([]);
    }
  };

  const handlePlayTrack = (track: any) => {
    playTrack(track, searchResults.length > 0 ? searchResults : mockTracks);
  };

  return (
    <div className="ml-60 pb-24 min-h-screen p-6">
      <h1 className="text-3xl font-bold mb-8">Search</h1>
      
      <div className="max-w-xl mb-8">
        <div className="relative">
          <Input
            type="text"
            placeholder="What do you want to listen to?"
            value={searchQuery}
            onChange={(e) => handleSearch(e.target.value)}
            className="w-full bg-white text-black px-4 py-3 pl-12 rounded-full focus:ring-2 focus:ring-spotify-green border-none"
          />
          <SearchIcon className="absolute left-4 top-1/2 transform -translate-y-1/2 w-4 h-4 text-black" />
        </div>
      </div>

      {searchResults.length > 0 ? (
        <section>
          <h2 className="text-2xl font-bold mb-6">Search Results</h2>
          <div className="space-y-2">
            {searchResults.map((track, index) => (
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
                <span className="text-spotify-text text-sm w-12">
                  {Math.floor(track.duration / 60)}:{(track.duration % 60).toString().padStart(2, '0')}
                </span>
              </div>
            ))}
          </div>
        </section>
      ) : (
        <section>
          <h2 className="text-2xl font-bold mb-6">Browse all</h2>
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {genres.map((genre) => {
              const Icon = genre.icon;
              return (
                <div 
                  key={genre.name}
                  className={`bg-gradient-to-br ${genre.color} p-6 rounded-lg aspect-square relative overflow-hidden cursor-pointer hover:scale-105 transition-transform`}
                >
                  <h3 className="text-2xl font-bold mb-2">{genre.name}</h3>
                  <div className="absolute -bottom-2 -right-2 w-20 h-20 transform rotate-12">
                    <Icon className="w-16 h-16 opacity-80" />
                  </div>
                </div>
              );
            })}
          </div>
        </section>
      )}
    </div>
  );
}
