import { useCallback, useState } from 'react';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import Home from './pages/Home/Home.jsx';
import SavedRoom from './pages/SavedRoom/SavedRoom.jsx';

function App() {
  const [homeDataCache, setHomeDataCache] = useState(null);

  const handleHomeDataCache = useCallback((nextData) => {
    setHomeDataCache((currentData) => {
      if (!currentData && nextData) {
        return nextData;
      }

      return currentData || nextData;
    });
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home homeDataCache={homeDataCache} onHomeDataCache={handleHomeDataCache} />} />
        <Route path="/saved-rooms" element={<SavedRoom />} />
        <Route path="/saved-rooms/:roomId" element={<SavedRoom />} />
        <Route path="/room-detail/:roomId" element={<Home homeDataCache={homeDataCache} onHomeDataCache={handleHomeDataCache} />} />
        <Route path="/room-detail" element={<Navigate to="/" replace />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;