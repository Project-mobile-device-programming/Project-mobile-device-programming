import { Link, useLocation } from 'wouter';
import { Home, Search, Library, Heart, Download, Music, User, Settings } from 'lucide-react';
import { useAuth } from '@/hooks/useAuth';
import { cn } from '@/lib/utils';

const navigation = [
  { name: 'Home', href: '/', icon: Home },
  { name: 'Search', href: '/search', icon: Search },
  { name: 'Your Library', href: '/library', icon: Library },
  { name: 'Liked Songs', href: '/favorites', icon: Heart },
  { name: 'Downloads', href: '/downloads', icon: Download },
];

export default function Sidebar() {
  const [location] = useLocation();
  const { user } = useAuth();

  return (
    <aside className="fixed left-0 top-0 w-60 h-full bg-spotify-gray p-6 overflow-y-auto z-40">
      <div className="mb-8">
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 bg-spotify-green rounded-full flex items-center justify-center">
            <Music className="w-4 h-4 text-white" />
          </div>
          <h1 className="text-xl font-bold">Spiketune</h1>
        </div>
      </div>

      <nav className="space-y-4">
        {navigation.map((item) => {
          const Icon = item.icon;
          const isActive = location === item.href;
          
          return (
            <Link key={item.name} href={item.href}>
              <a className={cn(
                "flex items-center gap-4 p-3 rounded-lg transition-colors",
                isActive 
                  ? "bg-spotify-light-gray text-white" 
                  : "text-spotify-text hover:text-white hover:bg-spotify-light-gray/50"
              )}>
                <Icon className="w-5 h-5" />
                <span>{item.name}</span>
              </a>
            </Link>
          );
        })}
      </nav>

      <div className="mt-8 pt-4 border-t border-spotify-light-gray">
        <h3 className="text-sm font-semibold text-spotify-text mb-4">RECENTLY PLAYED</h3>
        <div className="space-y-3">
          <div className="flex items-center gap-3 p-2 rounded hover:bg-spotify-light-gray transition-colors cursor-pointer">
            <img 
              src="https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?ixlib=rb-4.0.3&auto=format&fit=crop&w=50&h=50" 
              alt="Album cover" 
              className="w-10 h-10 rounded"
            />
            <div>
              <p className="text-sm">Chill Vibes</p>
              <p className="text-xs text-spotify-text">Playlist</p>
            </div>
          </div>
          <div className="flex items-center gap-3 p-2 rounded hover:bg-spotify-light-gray transition-colors cursor-pointer">
            <img 
              src="https://images.unsplash.com/photo-1571330735066-03aaa9429d89?ixlib=rb-4.0.3&auto=format&fit=crop&w=50&h=50" 
              alt="Album cover" 
              className="w-10 h-10 rounded"
            />
            <div>
              <p className="text-sm">Electronic Beats</p>
              <p className="text-xs text-spotify-text">Album</p>
            </div>
          </div>
        </div>
      </div>

      <div className="mt-8">
        <Link href="/profile">
          <a className="flex items-center gap-3 p-3 rounded hover:bg-spotify-light-gray transition-colors">
            {user?.profileImageUrl ? (
              <img 
                src={user.profileImageUrl} 
                alt="Profile" 
                className="w-8 h-8 rounded-full"
              />
            ) : (
              <User className="w-8 h-8 text-spotify-text" />
            )}
            <span className="text-sm">
              {user?.firstName || user?.email || 'User'}
            </span>
          </a>
        </Link>
      </div>
    </aside>
  );
}
