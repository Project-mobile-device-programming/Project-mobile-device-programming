import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Play, ChevronLeft, ChevronRight, Settings } from 'lucide-react';
import { useAudio } from '@/hooks/useAudio';
import { mockTracks } from '@/lib/mockData';
import { cn } from '@/lib/utils';

export default function Home() {
  const { playTrack } = useAudio();

  const trendingTracks = mockTracks.slice(0, 5);
  const recentTracks = mockTracks.slice(2, 5);
  const quickAccess = mockTracks.slice(0, 3);

  const handlePlayTrack = (track: any, queue = mockTracks) => {
    const trackIndex = queue.findIndex(t => t.id === track.id);
    playTrack(track, queue, trackIndex);
  };

  return (
    <div className="ml-60 pb-24 min-h-screen">
      {/* Top Bar */}
      <header className="sticky top-0 bg-spotify-gray/80 backdrop-blur-md z-30 p-6 flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Button variant="ghost" size="sm" className="w-8 h-8 bg-spotify-light-gray rounded-full hover:bg-spotify-text/20">
            <ChevronLeft className="w-4 h-4" />
          </Button>
          <Button variant="ghost" size="sm" className="w-8 h-8 bg-spotify-light-gray rounded-full hover:bg-spotify-text/20">
            <ChevronRight className="w-4 h-4" />
          </Button>
        </div>
        <div className="flex items-center gap-4">
          <Button variant="ghost" size="sm" className="text-spotify-text hover:text-white">
            <Settings className="w-4 h-4" />
          </Button>
          <Button className="bg-spotify-green text-white px-6 py-2 rounded-full font-semibold hover:bg-spotify-green-light">
            Premium
          </Button>
        </div>
      </header>

      {/* Content */}
      <div className="p-6">
        <h1 className="text-3xl font-bold mb-8">Good evening</h1>
        
        {/* Quick Access */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mb-8">
          {quickAccess.map((track) => (
            <div 
              key={track.id}
              className="bg-spotify-light-gray/50 rounded-lg p-4 flex items-center gap-4 hover:bg-spotify-light-gray transition-colors cursor-pointer group"
              onClick={() => handlePlayTrack(track)}
            >
              <img 
                src={track.coverUrl} 
                alt={track.title} 
                className="w-12 h-12 rounded"
              />
              <span className="font-semibold">{track.title}</span>
              <Button 
                size="sm"
                className="ml-auto w-10 h-10 bg-spotify-green rounded-full opacity-0 group-hover:opacity-100 transition-opacity hover:scale-105"
              >
                <Play className="w-4 h-4 ml-0.5" />
              </Button>
            </div>
          ))}
        </div>

        {/* Trending Music */}
        <section className="mb-8">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-2xl font-bold">Trending Now</h2>
            <Button variant="ghost" className="text-spotify-text hover:text-white text-sm font-semibold">
              Show all
            </Button>
          </div>
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-6">
            {trendingTracks.map((track) => (
              <div 
                key={track.id}
                className="bg-spotify-light-gray/30 p-4 rounded-lg hover:bg-spotify-light-gray/50 transition-colors cursor-pointer group"
                onClick={() => handlePlayTrack(track)}
              >
                <div className="relative mb-4">
                  <img 
                    src={track.coverUrl} 
                    alt={track.title} 
                    className="w-full aspect-square object-cover rounded-lg shadow-lg"
                  />
                  <Button 
                    size="sm"
                    className="absolute bottom-2 right-2 w-12 h-12 bg-spotify-green rounded-full opacity-0 group-hover:opacity-100 transition-opacity shadow-lg hover:scale-105"
                  >
                    <Play className="w-5 h-5 ml-1" />
                  </Button>
                </div>
                <h3 className="font-semibold mb-1">{track.title}</h3>
                <p className="text-spotify-text text-sm">{track.artist}</p>
              </div>
            ))}
          </div>
        </section>

        {/* Recently Played */}
        <section className="mb-8">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-2xl font-bold">Recently Played</h2>
            <Button variant="ghost" className="text-spotify-text hover:text-white text-sm font-semibold">
              Show all
            </Button>
          </div>
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-6">
            {recentTracks.map((track) => (
              <div 
                key={track.id}
                className="bg-spotify-light-gray/30 p-4 rounded-lg hover:bg-spotify-light-gray/50 transition-colors cursor-pointer group"
                onClick={() => handlePlayTrack(track)}
              >
                <div className="relative mb-4">
                  <img 
                    src={track.coverUrl} 
                    alt={track.title} 
                    className="w-full aspect-square object-cover rounded-lg shadow-lg"
                  />
                  <Button 
                    size="sm"
                    className="absolute bottom-2 right-2 w-12 h-12 bg-spotify-green rounded-full opacity-0 group-hover:opacity-100 transition-opacity shadow-lg hover:scale-105"
                  >
                    <Play className="w-5 h-5 ml-1" />
                  </Button>
                </div>
                <h3 className="font-semibold mb-1">{track.title}</h3>
                <p className="text-spotify-text text-sm">{track.artist}</p>
              </div>
            ))}
          </div>
        </section>
      </div>
    </div>
  );
}
