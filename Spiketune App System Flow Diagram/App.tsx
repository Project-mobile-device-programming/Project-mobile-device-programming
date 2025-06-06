import { useState } from 'react'
import './App.css'
import spotifyFlowDiagram from './assets/spotify_flow.png'

function App() {
  const [isZoomed, setIsZoomed] = useState(false);

  const toggleZoom = () => {
    setIsZoomed(!isZoomed);
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-gray-900 to-gray-800 text-white">
      <header className="py-6 px-4 md:px-8 border-b border-gray-700">
        <div className="container mx-auto">
          <h1 className="text-3xl md:text-4xl font-bold text-green-400">Spotify System Architecture</h1>
          <p className="mt-2 text-gray-300">A visual representation of Spotify's microservices architecture and system flow</p>
        </div>
      </header>

      <main className="container mx-auto py-8 px-4 md:px-8">
        <section className="mb-12">
          <h2 className="text-2xl font-semibold mb-4">System Flow Diagram</h2>
          <div className="bg-gray-800 p-4 rounded-lg shadow-lg">
            <div className={`relative overflow-hidden transition-all duration-300 ${isZoomed ? 'cursor-zoom-out' : 'cursor-zoom-in'}`}>
              <img 
                src={spotifyFlowDiagram} 
                alt="Spotify System Flow Diagram" 
                className={`w-full h-auto transition-transform duration-300 ${isZoomed ? 'scale-150' : 'scale-100'}`}
                onClick={toggleZoom}
              />
              {isZoomed && (
                <div className="absolute top-2 right-2 bg-gray-800 bg-opacity-70 p-2 rounded-md">
                  <span className="text-sm">Click to zoom out</span>
                </div>
              )}
            </div>
            <div className="mt-2 text-sm text-gray-400 text-center">
              Click on the diagram to zoom in/out
            </div>
          </div>
        </section>

        <section className="mb-12">
          <h2 className="text-2xl font-semibold mb-4">Architecture Overview</h2>
          <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
            <p className="mb-4">
              Spotify's architecture is built on a microservices foundation, allowing for scalability, resilience, and rapid development cycles. 
              The system consists of hundreds of small, independent services that communicate with each other to deliver a seamless music streaming experience.
            </p>
            
            <h3 className="text-xl font-medium text-green-400 mt-6 mb-2">Key Components</h3>
            <ul className="space-y-3">
              <li>
                <span className="font-medium text-green-300">Client Apps:</span> Mobile, web, and desktop applications that users interact with.
              </li>
              <li>
                <span className="font-medium text-green-300">Load Balancer:</span> Distributes incoming traffic across multiple servers to ensure optimal resource utilization.
              </li>
              <li>
                <span className="font-medium text-green-300">API Gateway/Access Point:</span> Central entry point that routes requests to appropriate microservices.
              </li>
              <li>
                <span className="font-medium text-green-300">Core Microservices:</span> Independent services handling specific functions like authentication, search, playback, playlists, recommendations, social features, and payment processing.
              </li>
              <li>
                <span className="font-medium text-green-300">Data Stores:</span> Various databases (Cassandra, PostgreSQL) storing user data, song metadata, and playlist information.
              </li>
              <li>
                <span className="font-medium text-green-300">Message Queue:</span> Facilitates asynchronous communication between services using technologies like Kafka.
              </li>
              <li>
                <span className="font-medium text-green-300">Data Warehouse:</span> Stores and processes large volumes of data for analytics and recommendation systems.
              </li>
              <li>
                <span className="font-medium text-green-300">Recommendation Engine:</span> Uses machine learning algorithms (Latent Factor Models) to provide personalized music recommendations.
              </li>
              <li>
                <span className="font-medium text-green-300">Content Delivery Network (CDN):</span> Distributes audio files globally to reduce latency and improve streaming performance.
              </li>
            </ul>
          </div>
        </section>

        <section>
          <h2 className="text-2xl font-semibold mb-4">System Flow</h2>
          <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
            <p className="mb-4">
              The diagram illustrates how different components interact within Spotify's architecture:
            </p>
            
            <ol className="space-y-3 list-decimal pl-5">
              <li>Users interact with Spotify through client applications (mobile, web, desktop).</li>
              <li>Client requests are routed through load balancers to the API Gateway/Access Point.</li>
              <li>The API Gateway directs requests to appropriate microservices based on the request type.</li>
              <li>Microservices communicate with databases and each other to fulfill requests.</li>
              <li>Audio streaming is handled through a Content Delivery Network (CDN) for optimal performance.</li>
              <li>Events are published to message queues for asynchronous processing.</li>
              <li>The Data Warehouse collects and processes events for analytics and recommendations.</li>
              <li>The Recommendation Engine uses processed data to generate personalized content suggestions.</li>
            </ol>
            
            <p className="mt-4">
              This microservices architecture allows Spotify to handle millions of concurrent users while maintaining low latency and high availability.
            </p>
          </div>
        </section>
      </main>

      <footer className="py-6 px-4 md:px-8 border-t border-gray-700 mt-12">
        <div className="container mx-auto text-center text-gray-400">
          <p>© {new Date().getFullYear()} Spotify System Architecture Visualization</p>
          <p className="mt-2 text-sm">Created with React and Tailwind CSS</p>
        </div>
      </footer>
    </div>
  )
}

export default App
