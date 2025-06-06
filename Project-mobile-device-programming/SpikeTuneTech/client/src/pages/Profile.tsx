import { useAuth } from '@/hooks/useAuth';

export default function Profile() {
  const { user } = useAuth();

  const topGenres = [
    { name: 'Electronic', percentage: 75 },
    { name: 'Pop', percentage: 50 },
    { name: 'Jazz', percentage: 33 },
  ];

  const recentArtists = [
    { name: 'Alex Rivera', genre: 'Electronic', image: 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?ixlib=rb-4.0.3&auto=format&fit=crop&w=60&h=60' },
    { name: 'Luna Star', genre: 'Pop', image: 'https://images.unsplash.com/photo-1494790108755-2616b612b19b?ixlib=rb-4.0.3&auto=format&fit=crop&w=60&h=60' },
    { name: 'Jazz Collective', genre: 'Jazz', image: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=60&h=60' },
  ];

  return (
    <div className="ml-60 pb-24 min-h-screen p-6">
      <div className="flex items-center gap-6 mb-8">
        <img 
          src={user?.profileImageUrl || "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&h=200"} 
          alt="Profile" 
          className="w-48 h-48 rounded-full shadow-lg object-cover"
        />
        <div>
          <p className="text-sm font-semibold mb-2">PROFILE</p>
          <h1 className="text-5xl font-bold mb-4">
            {user?.firstName || user?.email || 'User'}
          </h1>
          <p className="text-spotify-text">3 public playlists • 127 followers • 89 following</p>
        </div>
      </div>

      <div className="grid md:grid-cols-2 gap-8">
        <div>
          <h2 className="text-2xl font-bold mb-4">Recently Played Artists</h2>
          <div className="space-y-4">
            {recentArtists.map((artist) => (
              <div key={artist.name} className="flex items-center gap-4">
                <img 
                  src={artist.image} 
                  alt={artist.name} 
                  className="w-12 h-12 rounded-full object-cover"
                />
                <div>
                  <h3 className="font-semibold">{artist.name}</h3>
                  <p className="text-spotify-text text-sm">{artist.genre}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
        <div>
          <h2 className="text-2xl font-bold mb-4">Top Genres</h2>
          <div className="space-y-3">
            {topGenres.map((genre) => (
              <div key={genre.name} className="flex justify-between items-center">
                <span>{genre.name}</span>
                <div className="w-32 bg-spotify-light-gray rounded-full h-2">
                  <div 
                    className="bg-spotify-green h-2 rounded-full transition-all duration-300"
                    style={{ width: `${genre.percentage}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
