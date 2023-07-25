import Body from "./components/body/body";
import Login from "./components/login/login";
import { useState } from "react";

import {
  BrowserRouter as Router,
  Routes,
  Route,
} from "react-router-dom";

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/body/:token" element={<Body />} />
        </Routes>
      </Router>
    </>
  );
}

export default App;
