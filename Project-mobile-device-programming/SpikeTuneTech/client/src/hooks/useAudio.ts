import { useState, useEffect, useRef, useCallback } from 'react';
import type { Track, PlayerState } from '@/types/music';

export function useAudio() {
  const audioRef = useRef<HTMLAudioElement | null>(null);
  const [playerState, setPlayerState] = useState<PlayerState>({
    currentTrack: null,
    isPlaying: false,
    currentTime: 0,
    duration: 0,
    volume: 0.75,
    isShuffle: false,
    isRepeat: false,
    queue: [],
    queueIndex: -1,
  });

  // Initialize audio element
  useEffect(() => {
    audioRef.current = new Audio();
    const audio = audioRef.current;

    const handleTimeUpdate = () => {
      setPlayerState(prev => ({
        ...prev,
        currentTime: audio.currentTime,
      }));
    };

    const handleLoadedMetadata = () => {
      setPlayerState(prev => ({
        ...prev,
        duration: audio.duration,
      }));
    };

    const handleEnded = () => {
      handleNext();
    };

    audio.addEventListener('timeupdate', handleTimeUpdate);
    audio.addEventListener('loadedmetadata', handleLoadedMetadata);
    audio.addEventListener('ended', handleEnded);

    return () => {
      audio.removeEventListener('timeupdate', handleTimeUpdate);
      audio.removeEventListener('loadedmetadata', handleLoadedMetadata);
      audio.removeEventListener('ended', handleEnded);
      audio.pause();
    };
  }, []);

  // Update audio volume when volume state changes
  useEffect(() => {
    if (audioRef.current) {
      audioRef.current.volume = playerState.volume;
    }
  }, [playerState.volume]);

  const playTrack = useCallback((track: Track, queue: Track[] = [], startIndex: number = 0) => {
    if (!audioRef.current) return;

    const audio = audioRef.current;
    
    // If we have an audio URL, use it; otherwise just update the UI
    if (track.audioUrl) {
      audio.src = track.audioUrl;
      audio.load();
    }

    setPlayerState(prev => ({
      ...prev,
      currentTrack: track,
      isPlaying: true,
      queue: queue.length > 0 ? queue : [track],
      queueIndex: queue.length > 0 ? startIndex : 0,
      currentTime: 0,
    }));

    // Only play if we have an actual audio URL
    if (track.audioUrl) {
      audio.play().catch(console.error);
    }
  }, []);

  const togglePlayPause = useCallback(() => {
    if (!audioRef.current || !playerState.currentTrack) return;

    const audio = audioRef.current;

    if (playerState.isPlaying) {
      audio.pause();
      setPlayerState(prev => ({ ...prev, isPlaying: false }));
    } else {
      // Only play if we have an actual audio URL
      if (playerState.currentTrack.audioUrl) {
        audio.play().catch(console.error);
      }
      setPlayerState(prev => ({ ...prev, isPlaying: true }));
    }
  }, [playerState.isPlaying, playerState.currentTrack]);

  const handleNext = useCallback(() => {
    const { queue, queueIndex, isShuffle } = playerState;
    if (queue.length === 0) return;

    let nextIndex: number;
    if (isShuffle) {
      nextIndex = Math.floor(Math.random() * queue.length);
    } else {
      nextIndex = (queueIndex + 1) % queue.length;
    }

    const nextTrack = queue[nextIndex];
    if (nextTrack) {
      playTrack(nextTrack, queue, nextIndex);
    }
  }, [playerState, playTrack]);

  const handlePrevious = useCallback(() => {
    const { queue, queueIndex } = playerState;
    if (queue.length === 0) return;

    const prevIndex = queueIndex > 0 ? queueIndex - 1 : queue.length - 1;
    const prevTrack = queue[prevIndex];
    if (prevTrack) {
      playTrack(prevTrack, queue, prevIndex);
    }
  }, [playerState, playTrack]);

  const seek = useCallback((time: number) => {
    if (!audioRef.current) return;

    audioRef.current.currentTime = time;
    setPlayerState(prev => ({ ...prev, currentTime: time }));
  }, []);

  const setVolume = useCallback((volume: number) => {
    setPlayerState(prev => ({ ...prev, volume }));
  }, []);

  const toggleShuffle = useCallback(() => {
    setPlayerState(prev => ({ ...prev, isShuffle: !prev.isShuffle }));
  }, []);

  const toggleRepeat = useCallback(() => {
    setPlayerState(prev => ({ ...prev, isRepeat: !prev.isRepeat }));
  }, []);

  return {
    playerState,
    playTrack,
    togglePlayPause,
    handleNext,
    handlePrevious,
    seek,
    setVolume,
    toggleShuffle,
    toggleRepeat,
  };
}
