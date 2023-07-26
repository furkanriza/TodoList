import { useNavigate } from "react-router-dom";

const NotFound = () => {
  const navigateL = useNavigate();
  const navigateLoginPage = () => {
    navigateL('/');
  }

  return (
    <div className="notFound">
      <h2>404 Not Found</h2>
      <p>The page you are looking for does not exist.</p>
      <button onClick={navigateLoginPage}>Go to Home Page</button>
    </div>
  );
};

export default NotFound;
