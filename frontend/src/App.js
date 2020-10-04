import React from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import Home from "./views/Home";
import NewContact from "./views/NewContact";
import EditContact from "./views/EditContact";
import PageNotFound from "./views/PageNotFound";

function App() {
  return (
    <BrowserRouter>
      <Switch>
        <Route path="/" component={Home} exact />
        <Route path="/new" component={NewContact} exact />
        <Route path="/edit" component={EditContact} exact />
        <Route component={PageNotFound} />
      </Switch>
    </BrowserRouter>
  );
}

export default App;
