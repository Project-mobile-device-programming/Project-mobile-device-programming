import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { Music, Play, Heart, Download } from 'lucide-react';

export default function Landing() {
  const handleLogin = () => {
    window.location.href = '/api/login';
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-spotify-dark to-spotify-gray text-white">
      {/* Header */}
      <header className="p-6 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 bg-spotify-green rounded-full flex items-center justify-center">
            <Music className="w-4 h-4 text-white" />
          </div>
          <h1 className="text-xl font-bold">Spiketune</h1>
        </div>
        <Button 
          onClick={handleLogin}
          className="bg-spotify-green hover:bg-spotify-green-light text-white font-semibold px-8 py-2 rounded-full"
        >
          Log In
        </Button>
      </header>

      {/* Hero Section */}
      <main className="container mx-auto px-6 py-20">
        <div className="text-center mb-16">
          <h1 className="text-6xl font-bold mb-6">
            Your soundtrack
            <br />
            <span className="text-spotify-green">Your story</span>
          </h1>
          <p className="text-xl text-spotify-text mb-8 max-w-2xl mx-auto">
            Discover millions of songs, create your perfect playlists, and enjoy your music anywhere with Spiketune.
          </p>
          <Button 
            onClick={handleLogin}
            size="lg"
            className="bg-spotify-green hover:bg-spotify-green-light text-white font-semibold px-12 py-4 rounded-full text-lg"
          >
            Get Started
          </Button>
        </div>

        {/* Features */}
        <div className="grid md:grid-cols-3 gap-8 mb-20">
          <Card className="bg-spotify-light-gray border-none">
            <CardContent className="p-8 text-center">
              <div className="w-16 h-16 bg-spotify-green rounded-full flex items-center justify-center mx-auto mb-6">
                <Play className="w-8 h-8 text-white" />
              </div>
              <h3 className="text-xl font-semibold mb-4 text-white">Unlimited Music</h3>
              <p className="text-spotify-text">
                Stream millions of songs with high-quality audio and discover new favorites every day.
              </p>
            </CardContent>
          </Card>

          <Card className="bg-spotify-light-gray border-none">
            <CardContent className="p-8 text-center">
              <div className="w-16 h-16 bg-spotify-green rounded-full flex items-center justify-center mx-auto mb-6">
                <Heart className="w-8 h-8 text-white" />
              </div>
              <h3 className="text-xl font-semibold mb-4 text-white">Personal Playlists</h3>
              <p className="text-spotify-text">
                Create custom playlists, save your favorite songs, and organize your music your way.
              </p>
            </CardContent>
          </Card>

          <Card className="bg-spotify-light-gray border-none">
            <CardContent className="p-8 text-center">
              <div className="w-16 h-16 bg-spotify-green rounded-full flex items-center justify-center mx-auto mb-6">
                <Download className="w-8 h-8 text-white" />
              </div>
              <h3 className="text-xl font-semibold mb-4 text-white">Offline Listening</h3>
              <p className="text-spotify-text">
                Download your favorite tracks and listen anywhere, even without an internet connection.
              </p>
            </CardContent>
          </Card>
        </div>

        {/* Call to Action */}
        <div className="text-center">
          <h2 className="text-3xl font-bold mb-6">Ready to start your musical journey?</h2>
          <Button 
            onClick={handleLogin}
            size="lg"
            className="bg-spotify-green hover:bg-spotify-green-light text-white font-semibold px-12 py-4 rounded-full text-lg"
          >
            Join Spiketune
          </Button>
        </div>
      </main>
    </div>
  );
}
