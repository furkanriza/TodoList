import React, { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import './login.css';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isLoginFailed, setLoginFailed] = useState(false);

    const navigate = useNavigate();


    useEffect(() => {
        sessionStorage.clear();
    }, []);

    const handleLogin = (e) => {
        e.preventDefault();
        if (validate()) {
            const user = { username, password };
            fetch('http://localhost:8080/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(user),
            })
                .then((res) => {
                    console.log("Response status:", res.status);
                    if (res.ok) {
                        console.log("Response returned ok");
                        return res.json();
                    } else {
                        console.log("Login failed with status:", res.status);
                        setLoginFailed(true);
                        throw new Error('Login failed');
                    }
                })
                .then((resp) => {
                    console.log("Generated token:", resp.jwt);
                    toast.success('Success');
                    sessionStorage.setItem('username', username);
                    sessionStorage.setItem('userrole', resp.role);
                    localStorage.setItem('token', resp.jwt);
                    navigate('/home');
                })
                .catch((err) => {
                    console.log("Error during login:", err.message);
                    toast.error('Login failed due to: ' + err.message);
                });
        }
    };



    const validate = () => {
        let result = true;
        if ((username === '' || username === '') && (password === '' || password === null)) {
            result = false;
            alert('Please enter username and password');
        }
        else {
            if (username === '' || username === null) {
                result = false;
                alert('Please enter a username');
            }
            if (password === '' || password === null) {
                result = false;
                alert('Please enter a password');
            }
        }

        return result;
    };

    return (

        <div className='entireThing'>
            <section>
                <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span> <span></span>

                <div className="signin">
                    <div className="content">
                        <h2>Sign In</h2>
                        <div className="form">
                            <div className="inputBox">
                                <input type="text" required value={username} onChange={(e) => setUsername(e.target.value)} />
                                <i>Username</i>
                            </div>
                            <div className="inputBox">
                                <input type="password" required value={password} onChange={(e) => setPassword(e.target.value)} />
                                <i>Password</i>
                            </div>
                            <div className="inputBox">
                                <input type="submit" value="Login" onClick={handleLogin} />
                            </div>
                            {isLoginFailed && <div className="invalidLoginDiv">
                                <label className="invalidLogin">invalid username or password</label>
                            </div>}
                            <div className="links" >
                                <label>Don't have an account yet?</label>
                                <input className="registerButton" type="submit" value="Sign Up Now" /*onClick={routeRegisterPage}*/ />
                            </div>
                        </div>
                    </div>
                </div >
            </section >
        </div >

    );
};

export default Login;