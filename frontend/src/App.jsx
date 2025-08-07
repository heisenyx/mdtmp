import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { Toaster } from 'react-hot-toast'
import HomePage from './pages/HomePage'
import PublicationPage from './pages/PublicationPage'

function App() {

  return (
    <BrowserRouter>
      <Toaster />

      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/p/:hash" element={<PublicationPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
