import { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import "./home.css";

function Home() {
    const [todos, setTodos] = useState([]);
    const [youTubeVideos, setYouTubeVideos] = useState([]);

    const [filter, setFilter] = useState('all');

    const [accessToken, setAccessToken] = useState(localStorage.getItem('accesstoken'));
    const [token, setToken] = useState(localStorage.getItem('token'));
    const navigate = useNavigate();
    const [isTokenValid, setIsTokenValid] = useState(true);
    const [isLoading, setIsLoading] = useState(true);

    /*useEffect(()=>{
        setInterval(() => {
            
        }, 5000);
    }, []);*/ //way 1

    // check if storedToken is null or invalid or expired
    useEffect(() => {
        const validateTokenAndNavigate = async () => {
            if (!accessToken) {
                setIsTokenValid(false); // Navigate to the login page if token is null
                setIsLoading(false);
            } else {
                try {
                    const response = await fetch("http://localhost:8080/user/", {
                        headers: {
                            'Authorization': `Bearer ${accessToken}`,
                            'Content-Type': 'application/json'
                        },
                    });

                    if (!response.ok) {
                        setIsTokenValid(false);
                        setIsLoading(false);
                    } else {
                        setIsLoading(false);
                        fetchTodos();
                    }
                } catch (error) {
                    console.log('Error verifying token:', error);
                    setIsTokenValid(false);  // Navigate to the login page if there's an error during token verification
                    setIsLoading(false);
                }
            }
        };

        validateTokenAndNavigate();
    }, [accessToken]);


    useEffect(() => {
        if (!isTokenValid) {
            refreshTheToken();
        }
    }, [isTokenValid]);

    const refreshTheToken = async () => {
        if (!token) {
            alert('An error occurred while retrieving data.');
            navigateToLogin();
            return;
        }

        try {
            // Send a request to your backend to refresh the access token using the refresh token
            const refreshResponse = await fetch('http://localhost:8080/auth/refreshToken', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    token: token,
                }),
            });

            if (!refreshResponse.ok) {
                console.log("33");
                // Refresh token request failed, handle accordingly (e.g., show error, log out user)
                alert('An error occurred while retrieving data.');
                navigateToLogin();
                return;
            }

            console.log("refreshResponse: ", refreshResponse);
            const refreshedTokens = await refreshResponse.json();
            console.log("refreshedTokens: ", refreshedTokens);
            const newAccessToken = refreshedTokens.accessToken;
            console.log("newAccessToken", newAccessToken);
            const newRefreshToken = refreshedTokens.token;
            console.log("newRefreshToken", newRefreshToken);

            // Update the access token in localStorage
            localStorage.setItem('accesstoken', newAccessToken);
            setAccessToken(newAccessToken);
            localStorage.setItem('token', newRefreshToken);
            setToken(newRefreshToken);

            // Mark the token as valid and fetch todos with the new access token
            setIsTokenValid(true);
            fetchTodos();
        } catch (error) {
            console.log('Error refreshing token:', error);
            alert('An error occurred while retrieving data.');
            navigateToLogin();
        }
    };

    const navigateToLogin = () => {
        navigate('/login');
    };

    const handleLogout = () => {
        // Remove the token from localStorage
        localStorage.removeItem('token');

        // Navigate to the login page
        navigate('/login');
    };

    const fetchTodos = () => {
        fetch("http://localhost:8080/assignments/", {
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    console.log("Response status (fetchTodos):", response.status);
                    return response.json();
                } else {
                    // If the response status is 401 (Unauthorized), the token is invalid
                    if (response.status === 401) {
                        console.log("Invalid token");
                        setIsTokenValid(false);
                    } else {
                        console.log("Response status:", response.status);
                        throw new Error("Failed to fetch data");
                    }
                }
            })
            .then(data => {
                const updatedTodos = data.map(todo => ({
                    ...todo,
                    status: todo.status === "1" ? true : false
                }));

                // beginning of function (remove this part if you are not going to use youtube api)
                Promise.all(updatedTodos.map(todo => fetchSuggestedVideos(todo)))
                    .then(videoDataArray => {
                        const updatedTodosWithVideos = updatedTodos.map((todo, index) => ({
                            ...todo,
                            videos: videoDataArray[index] // Set the 'videos' property for each todo
                        }));
                        updatedTodosWithVideos.forEach(todo => {
                            console.log("Description:", todo.description);
                            console.log("Videos:");
                            todo.videos.forEach(video => {
                                console.log(video.videoid);
                            });
                        });
                        setTodos(updatedTodosWithVideos);
                    })
                    .catch(error => {
                        console.log('Error fetching videos:', error);
                        setTodos(updatedTodos); // Set todos without videos in case of error
                    });

                // end of funtion
                setTodos(updatedTodos);
            })
            .catch(error => {
                console.log('Error fetching todos:', error);
            });
    }

    const fetchSuggestedVideos = async (todo) => {
        //console.log(todo.description);
        let url = "http://localhost:8080/search/get?q=" + todo.description;
        //console.log(url);
        try {
            const response = await fetch(url) // Return the fetch promise
                ;
            console.log("Response status:", response.status);
            const data = await response.json();
            //console.log(data);
            const videoData = data.map(item => ({
                videoid: item.videoid,
                title: item.title,
                thumbnailurl: item.thumbnailurl,
            }));
            return videoData;
        } catch (error) {
            console.error('Error fetching data:', error);
            return [];
        }
    };


    useEffect(() => {
        const addTodo = (event) => {
            event.preventDefault();
            const newTodoText = event.target.elements.todoInput.value;
            if (newTodoText.trim() !== '') {
                const newTodo = {
                    description: newTodoText,
                    status: 0,
                    userID: 5
                };

                // Insert the new todo into the database
                fetch("http://localhost:8080/assignments/", {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${accessToken}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(newTodo)
                })
                    .then(response => response.json())
                    .then(data => {
                        // Fetch suggested videos for the new todo
                        fetchSuggestedVideos(data)
                            .then(videoData => {
                                // Update the state with the newly created todo and its related videos
                                const newTodoWithVideos = {
                                    ...data,
                                    status: data.status === 1 ? true : false,
                                    videos: videoData
                                };
                                setTodos([...todos, newTodoWithVideos]);
                            })
                            .catch(error => {
                                console.log('Error fetching videos for the new todo:', error);
                                // Set the new todo without videos in case of error
                                setTodos([...todos, { ...data, status: data.status === 1 ? true : false }]);
                            });
                    })
                    .catch(error => console.log('Error adding todo:', error));

                event.target.reset();
            }
        };


        document.addEventListener('submit', addTodo);

        return () => {
            document.removeEventListener('submit', addTodo);
        };
    }, [todos]);



    const toggleTodo = (todoId) => {
        const updatedTodos = todos.map(todo => {
            if (todo.id === todoId) {
                const updatedTodo = { ...todo, status: !todo.status ? 1 : 0 };
                console.log("updated todo\n", updatedTodo);

                // Send PATCH request to update the todo in the database
                fetch(`http://localhost:8080/assignments/${todoId}`, {
                    method: 'PATCH',
                    headers: {
                        'Authorization': `Bearer ${accessToken}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(updatedTodo)
                })
                    .then(response => response.json())
                    .then(data => {
                        // Update the state with the updated todo
                        const updatedTodo = { ...data, status: data.status === 1 ? true : false };
                        setTodos(todos.map(t => t.id === todoId ? updatedTodo : t));
                        setTodos(todos.map(todo =>
                            todo.id === todoId ? { ...todo, status: !todo.status } : todo
                        ));
                        console.log("updated!!!\n");
                    })
                    .catch(error => console.log('Error updating todo:', error));

                return updatedTodo;
            }
            return todo;
        });

        setTodos(updatedTodos);
    };



    const filterTodos = () => {
        switch (filter) {
            case 'active':
                return todos.filter(todo => !todo.status);
            case 'completed':
                return todos.filter(todo => todo.status);
            default:
                return todos;
        }
    };



    const clearCompleted = () => {
        // Get the ids of completed todos
        const completedTodoIds = todos.filter(todo => todo.status).map(todo => todo.id);

        // Send DELETE requests to delete the completed todos from the database
        completedTodoIds.forEach(todoId => {
            fetch(`http://localhost:8080/assignments/${todoId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) {
                        // Remove the completed todos from the state
                        setTodos(todos.filter(todo => !todo.status));
                        console.log("Items deleted\n");
                    } else {
                        console.log(`Error deleting todo ${todoId}:`, response.statusText);
                    }
                })
                .catch(error => console.log(`Error deleting todo ${todoId}:`, error));
        });
    };



    const deleteTodo = (todoId) => {
        // Send DELETE request to delete the todo from the database
        fetch(`http://localhost:8080/assignments/${todoId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    // Remove the todo from the state
                    console.log("Deleted item:\n", todos.filter(todo => todo.id === todoId));
                    setTodos(todos.filter(todo => todo.id !== todoId));
                } else {
                    console.log('Error deleting todo:', response.statusText);
                }
            })
            .catch(error => console.log('Error deleting todo:', error));
    };


    const [isVisible, setIsVisible] = useState(true);
    const hideContent = () => {
        setIsVisible(!isVisible);
        console.log(isVisible);
    };

    const [isVisibleVideos, setIsVisibleVideos] = useState(false);
    const showVideos = () => {
        setIsVisibleVideos(!isVisibleVideos);
    };



    if (isLoading) {
        return <div>Loading...</div>;
    }


    return (
        <div className='entirebody'>
            <section className="todoapp">
                <header className="header">
                    <h1>todos</h1>
                    <form>
                        <input className="new-todo" name="todoInput" placeholder="What needs to be done?" autoFocus />
                    </form>
                </header>

                <section className="main">
                    <input className="toggle-all" type="checkbox" />
                    <label onClick={() => hideContent()} htmlFor="toggle-all">
                        Mark all as complete
                    </label>
                    {isVisible &&
                        <ul className="todo-list">
                            {filterTodos().map(todo => (
                                <li key={todo.id} className={todo.status ? 'completed' : ''}>
                                    <div className="view">
                                        <input
                                            className="toggle"
                                            type="checkbox"
                                            checked={todo.status ? true : false}
                                            onChange={() => toggleTodo(todo.id)}
                                        />
                                        <label>{todo.description}
                                            {isVisibleVideos && <> <br /> Related videos:<br />
                                                {todo.videos && todo.videos != null && Array.isArray(todo.videos) && todo.videos.length > 0 && todo.videos.map((video, videoIndex) => (
                                                    <span key={videoIndex} className='tab'>
                                                        <a href={`https://www.youtube.com/watch?v=${video.videoid}`}><img src={`${video.title}`} /></a>
                                                    </span>
                                                ))}</>}</label>
                                        <button className="destroy" onClick={() => deleteTodo(todo.id)}></button>

                                    </div>
                                </li>
                            ))}
                        </ul>
                    }
                </section>

                <footer className="footer">
                    <span className="todo-count">
                        <strong>{filterTodos().filter(todo => !todo.status).length}</strong>
                        {filter === 'all' ? ' items left' : ''}
                    </span>

                    <ul className="filters">
                        <li>
                            <a
                                href="#/"
                                className={filter === 'all' ? 'selected' : ''}
                                onClick={() => setFilter('all')}
                            >
                                All
                            </a>
                        </li>
                        <li>
                            <a
                                href="#/"
                                className={filter === 'active' ? 'selected' : ''}
                                onClick={() => setFilter('active')}
                            >
                                Active
                            </a>
                        </li>
                        <li>
                            <a
                                href="#/"
                                className={filter === 'completed' ? 'selected' : ''}
                                onClick={() => setFilter('completed')}
                            >
                                Completed
                            </a>
                        </li>
                    </ul>


                    <button className="clear-completed" onClick={clearCompleted}>
                        Clear completed
                    </button>

                    <button className="clear-completed show-videos" style={{ marginRight: '2em' }} onClick={showVideos}>
                        Show Related Videos
                    </button>


                </footer>
            </section>
            <div className='log-out-div'>
                <button className="log-out" onClick={handleLogout}>
                    Log Out
                </button>
            </div>

        </div>
    );
}

export default Home;