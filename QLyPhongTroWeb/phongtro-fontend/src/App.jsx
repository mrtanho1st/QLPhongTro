import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import Home from './pages/Home/Home.jsx';
import SavedRoom from './pages/SavedRoom/SavedRoom.jsx';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/saved-rooms" element={<SavedRoom />} />
        <Route path="/saved-rooms/:roomId" element={<SavedRoom />} />
        <Route path="/room-detail/:roomId" element={<Home />} />
        <Route path="/room-detail" element={<Navigate to="/" replace />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;