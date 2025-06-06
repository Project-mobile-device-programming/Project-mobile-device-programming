import { useEffect, useState } from 'react';
import { Music } from 'lucide-react';

interface SplashScreenProps {
  onComplete: () => void;
}

export default function SplashScreen({ onComplete }: SplashScreenProps) {
  const [isVisible, setIsVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsVisible(false);
      setTimeout(onComplete, 300); // Allow fade out animation to complete
    }, 2000);

    return () => clearTimeout(timer);
  }, [onComplete]);

  if (!isVisible) {
    return (
      <div className="fixed inset-0 bg-gradient-to-br from-spotify-dark to-spotify-gray flex items-center justify-center z-50 opacity-0 transition-opacity duration-300" />
    );
  }

  return (
    <div className="fixed inset-0 bg-gradient-to-br from-spotify-dark to-spotify-gray flex items-center justify-center z-50">
      <div className="text-center animate-pulse">
        <div className="w-24 h-24 bg-spotify-green rounded-full flex items-center justify-center mb-6 mx-auto shadow-2xl">
          <Music className="w-8 h-8 text-white" />
        </div>
        <h1 className="text-4xl font-bold text-spotify-green mb-2">SPIKETUNE</h1>
        <p className="text-spotify-text text-lg">Your soundtrack - Your story</p>
      </div>
    </div>
  );
}
