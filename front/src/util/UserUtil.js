export function isUserRole(user, role) {

    return user.role == role;
}

export function isUserOneOfRoles(user, roles) {

    for(let role of roles) {

        if(isUserRole(user, role)) {
            return true;
        }
    }

    return false;
}