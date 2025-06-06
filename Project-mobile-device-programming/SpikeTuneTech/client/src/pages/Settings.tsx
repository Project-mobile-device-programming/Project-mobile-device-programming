import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { Switch } from '@/components/ui/switch';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { useAuth } from '@/hooks/useAuth';

export default function Settings() {
  const { user } = useAuth();
  const [audioQuality, setAudioQuality] = useState('high');
  const [crossfade, setCrossfade] = useState(true);
  const [theme, setTheme] = useState('dark');

  const handleLogout = () => {
    window.location.href = '/api/logout';
  };

  return (
    <div className="ml-60 pb-24 min-h-screen p-6">
      <h1 className="text-3xl font-bold mb-8">Settings</h1>
      
      <div className="max-w-2xl space-y-8">
        <section>
          <h2 className="text-xl font-semibold mb-4">Account</h2>
          <div className="space-y-4">
            <div className="flex justify-between items-center p-4 bg-spotify-light-gray/30 rounded-lg">
              <div>
                <h3 className="font-semibold">Email</h3>
                <p className="text-spotify-text text-sm">{user?.email || 'Not provided'}</p>
              </div>
              <Button variant="outline" size="sm" className="text-white border-white hover:bg-white hover:text-black">
                Edit
              </Button>
            </div>
            <div className="flex justify-between items-center p-4 bg-spotify-light-gray/30 rounded-lg">
              <div>
                <h3 className="font-semibold">Name</h3>
                <p className="text-spotify-text text-sm">
                  {user?.firstName ? `${user.firstName} ${user.lastName || ''}`.trim() : 'Not provided'}
                </p>
              </div>
              <Button variant="outline" size="sm" className="text-white border-white hover:bg-white hover:text-black">
                Edit
              </Button>
            </div>
          </div>
        </section>

        <section>
          <h2 className="text-xl font-semibold mb-4">Playback</h2>
          <div className="space-y-4">
            <div className="flex justify-between items-center p-4 bg-spotify-light-gray/30 rounded-lg">
              <div>
                <h3 className="font-semibold">Audio Quality</h3>
                <p className="text-spotify-text text-sm">High (320 kbps)</p>
              </div>
              <Select value={audioQuality} onValueChange={setAudioQuality}>
                <SelectTrigger className="w-40 bg-spotify-light-gray text-white border-spotify-light-gray">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent className="bg-spotify-light-gray border-spotify-light-gray">
                  <SelectItem value="high">High (320 kbps)</SelectItem>
                  <SelectItem value="normal">Normal (160 kbps)</SelectItem>
                  <SelectItem value="low">Low (96 kbps)</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="flex justify-between items-center p-4 bg-spotify-light-gray/30 rounded-lg">
              <div>
                <h3 className="font-semibold">Crossfade</h3>
                <p className="text-spotify-text text-sm">Smooth transition between tracks</p>
              </div>
              <Switch 
                checked={crossfade} 
                onCheckedChange={setCrossfade}
                className="data-[state=checked]:bg-spotify-green"
              />
            </div>
          </div>
        </section>

        <section>
          <h2 className="text-xl font-semibold mb-4">Appearance</h2>
          <div className="space-y-4">
            <div className="flex justify-between items-center p-4 bg-spotify-light-gray/30 rounded-lg">
              <div>
                <h3 className="font-semibold">Theme</h3>
                <p className="text-spotify-text text-sm">Dark mode</p>
              </div>
              <Select value={theme} onValueChange={setTheme}>
                <SelectTrigger className="w-32 bg-spotify-light-gray text-white border-spotify-light-gray">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent className="bg-spotify-light-gray border-spotify-light-gray">
                  <SelectItem value="dark">Dark</SelectItem>
                  <SelectItem value="light">Light</SelectItem>
                  <SelectItem value="auto">Auto</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
        </section>

        <section>
          <h2 className="text-xl font-semibold mb-4">Account Actions</h2>
          <div className="p-4 bg-spotify-light-gray/30 rounded-lg">
            <Button 
              onClick={handleLogout}
              variant="destructive"
              className="bg-red-600 hover:bg-red-700"
            >
              Log Out
            </Button>
          </div>
        </section>
      </div>
    </div>
  );
}
