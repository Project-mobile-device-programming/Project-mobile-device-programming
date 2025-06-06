import { useState } from 'react';
import { 
  Play, 
  Pause, 
  SkipBack, 
  SkipForward, 
  Shuffle, 
  Repeat, 
  Volume2, 
  Heart,
  ExternalLink,
  List,
  Monitor,
  Maximize2
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Slider } from '@/components/ui/slider';
import { useAudio } from '@/hooks/useAudio';
import { formatTime } from '@/lib/mockData';
import { cn } from '@/lib/utils';

export default function MusicPlayer() {
  const {
    playerState,
    togglePlayPause,
    handleNext,
    handlePrevious,
    seek,
    setVolume,
    toggleShuffle,
    toggleRepeat,
  } = useAudio();

  const { currentTrack, isPlaying, currentTime, duration, volume, isShuffle, isRepeat } = playerState;

  const handleSeek = (value: number[]) => {
    seek(value[0]);
  };

  const handleVolumeChange = (value: number[]) => {
    setVolume(value[0] / 100);
  };

  const progress = duration > 0 ? (currentTime / duration) * 100 : 0;

  if (!currentTrack) return null;

  return (
    <div className="fixed bottom-0 left-0 right-0 bg-spotify-gray border-t border-spotify-light-gray px-4 py-3 z-50">
      <div className="flex items-center justify-between">
        {/* Current Track Info */}
        <div className="flex items-center gap-4 w-80">
          <img 
            src={currentTrack.coverUrl || "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?ixlib=rb-4.0.3&auto=format&fit=crop&w=60&h=60"} 
            alt="Now Playing" 
            className="w-14 h-14 rounded shadow-lg"
          />
          <div className="min-w-0 flex-1">
            <h4 className="font-semibold truncate">{currentTrack.title}</h4>
            <p className="text-spotify-text text-sm truncate">{currentTrack.artist}</p>
          </div>
          <Button variant="ghost" size="sm" className="text-spotify-text hover:text-white">
            <Heart className="w-4 h-4" />
          </Button>
          <Button variant="ghost" size="sm" className="text-spotify-text hover:text-white">
            <ExternalLink className="w-4 h-4" />
          </Button>
        </div>

        {/* Player Controls */}
        <div className="flex flex-col items-center gap-2 flex-1 max-w-2xl">
          <div className="flex items-center gap-4">
            <Button
              variant="ghost"
              size="sm"
              onClick={toggleShuffle}
              className={cn(
                "text-spotify-text hover:text-white transition-colors",
                isShuffle && "text-spotify-green"
              )}
            >
              <Shuffle className="w-4 h-4" />
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={handlePrevious}
              className="text-spotify-text hover:text-white transition-colors"
            >
              <SkipBack className="w-4 h-4" />
            </Button>
            <Button
              onClick={togglePlayPause}
              className="w-10 h-10 bg-white text-black rounded-full hover:scale-105 transition-transform"
            >
              {isPlaying ? (
                <Pause className="w-4 h-4" />
              ) : (
                <Play className="w-4 h-4 ml-0.5" />
              )}
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={handleNext}
              className="text-spotify-text hover:text-white transition-colors"
            >
              <SkipForward className="w-4 h-4" />
            </Button>
            <Button
              variant="ghost"
              size="sm"
              onClick={toggleRepeat}
              className={cn(
                "text-spotify-text hover:text-white transition-colors",
                isRepeat && "text-spotify-green"
              )}
            >
              <Repeat className="w-4 h-4" />
            </Button>
          </div>
          <div className="flex items-center gap-3 w-full">
            <span className="text-xs text-spotify-text w-10 text-right">
              {formatTime(currentTime)}
            </span>
            <div className="flex-1">
              <Slider
                value={[progress]}
                onValueChange={handleSeek}
                max={100}
                step={0.1}
                className="w-full cursor-pointer"
              />
            </div>
            <span className="text-xs text-spotify-text w-10">
              {formatTime(duration)}
            </span>
          </div>
        </div>

        {/* Volume and Options */}
        <div className="flex items-center gap-4 w-80 justify-end">
          <Button variant="ghost" size="sm" className="text-spotify-text hover:text-white">
            <List className="w-4 h-4" />
          </Button>
          <Button variant="ghost" size="sm" className="text-spotify-text hover:text-white">
            <Monitor className="w-4 h-4" />
          </Button>
          <div className="flex items-center gap-2">
            <Button variant="ghost" size="sm" className="text-spotify-text hover:text-white">
              <Volume2 className="w-4 h-4" />
            </Button>
            <div className="w-20">
              <Slider
                value={[volume * 100]}
                onValueChange={handleVolumeChange}
                max={100}
                step={1}
                className="cursor-pointer"
              />
            </div>
          </div>
          <Button variant="ghost" size="sm" className="text-spotify-text hover:text-white">
            <Maximize2 className="w-4 h-4" />
          </Button>
        </div>
      </div>
    </div>
  );
}
