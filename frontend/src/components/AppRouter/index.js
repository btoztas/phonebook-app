import React from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import Home from "../../views/Home";
import NewContact from "../../views/NewContact";
import EditContact from "../../views/EditContact";
import PageNotFound from "../../views/PageNotFound";

/**
 * The Component where the App Routing Logic lives
 */
export default function AppRouter() {
  return (
    <BrowserRouter>
      <Switch>
        <Route path="/" component={Home} exact />
        <Route path="/new" component={NewContact} exact />
        <Route path="/edit/:contactId" component={EditContact} exact />
        <Route component={PageNotFound} />
      </Switch>
    </BrowserRouter>
  );
}
