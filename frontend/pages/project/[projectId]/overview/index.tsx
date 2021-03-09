import React from "react";
import CodeAnalysis from "../../../../components/CodeAnalysis";
import AuthView from "../../../../components/AuthView";
import MenuLayout from "../../../../components/layout/menu/MenuLayout";

const index = () => {
    return (
        <AuthView>
            <MenuLayout tabSelected={0}>
                <CodeAnalysis/>
            </MenuLayout>
        </AuthView>
    );
};

export default index;
