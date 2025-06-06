import { Download } from 'lucide-react';
import { mockTracks, formatTime } from '@/lib/mockData';
import { useAudio } from '@/hooks/useAudio';

export default function Downloads() {
  const { playTrack } = useAudio();

  // Mock downloaded songs - last 2 tracks
  const downloadedSongs = mockTracks.slice(-2);
  const totalSize = downloadedSongs.reduce((acc, song) => acc + 3.5, 0); // Mock file sizes

  const handlePlayTrack = (track: any) => {
    const trackIndex = downloadedSongs.findIndex(t => t.id === track.id);
    playTrack(track, downloadedSongs, trackIndex);
  };

  return (
    <div className="ml-60 pb-24 min-h-screen p-6">
      <h1 className="text-3xl font-bold mb-8">Downloaded Music</h1>
      
      <div className="flex items-center gap-4 mb-6">
        <div className="w-16 h-16 bg-spotify-green rounded-lg flex items-center justify-center">
          <Download className="w-8 h-8" />
        </div>
        <div>
          <h2 className="text-xl font-bold">Downloads</h2>
          <p className="text-spotify-text">{downloadedSongs.length} songs • {totalSize.toFixed(1)} MB</p>
        </div>
      </div>

      <div className="space-y-4">
        {downloadedSongs.map((track) => (
          <div 
            key={track.id}
            className="flex items-center gap-4 p-3 rounded hover:bg-spotify-light-gray/50 transition-colors cursor-pointer"
            onClick={() => handlePlayTrack(track)}
          >
            <img src={track.coverUrl} alt={track.title} className="w-10 h-10 rounded" />
            <div className="flex-1">
              <h3 className="font-semibold">{track.title}</h3>
              <p className="text-spotify-text text-sm">{track.artist}</p>
            </div>
            <div className="flex items-center gap-2">
              <Download className="w-4 h-4 text-spotify-green" />
              <span className="text-spotify-text text-sm">3.2 MB</span>
            </div>
            <span className="text-spotify-text text-sm w-12">{formatTime(track.duration)}</span>
          </div>
        ))}
      </div>
    </div>
  );
}
