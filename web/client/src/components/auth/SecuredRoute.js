import {Redirect, Route} from 'react-router-dom';
import {connect} from 'react-redux'
import Loading from './Loading';

const mapStateToProps = (state) => {
  return {
    isAuthenticated: state.user.isAuthenticated,
    isLoading: state.user.isLoading,
    hasCurrentUnit: !!state.user.unitContext
  };
};

const SecuredRoute = ({component: Component, isAuthenticated, isLoading, hasCurrentUnit, allowMissingUnit, ...rest}) => {

  if (!isAuthenticated && isLoading) {
    return
  <
    Loading / >
  }

  return (
    < Route
  {...
    rest
  }
  render = {(props)
=>
  {
    if (!isAuthenticated) {
      return
    <
      Redirect
      to = '/' / >;
    }

    return ( < Component
    {...
      props
    }
    />)
  }
}
  />
)
  ;
}

export default connect(mapStateToProps)(SecuredRoute);
