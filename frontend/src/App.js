import Body from "./components/body/body";
import Login from "./components/login/login";
import {
  BrowserRouter as Router,
  Routes,
  Route,
} from "react-router-dom";
//import Footer from "./components/footer/footer";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/body/:isAUser" element={<Body />} />
      </Routes>
    </Router>
  );
}

export default App;
