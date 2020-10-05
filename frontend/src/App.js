import React from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import Home from "./views/Home";
import NewContact from "./views/NewContact";
import EditContact from "./views/EditContact";
import PageNotFound from "./views/PageNotFound";
import TopBar from "./components/TopBar";

function App() {
  return (
    <>
      <TopBar />
      <BrowserRouter>
        <Switch>
          <Route path="/" component={Home} exact />
          <Route path="/new" component={NewContact} exact />
          <Route path="/edit/:contactId" component={EditContact} exact />
          <Route component={PageNotFound} />
        </Switch>
      </BrowserRouter>
    </>
  );
}

export default App;
