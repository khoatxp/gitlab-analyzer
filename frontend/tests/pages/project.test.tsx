import {createShallow} from "@material-ui/core/test-utils";
import React from 'react';
import Index from '../../pages/project/[serverId]';
import {RouterContext} from "next/dist/next-server/lib/router-context";

describe("Project Folder", () =>{
    // @ts-ignore
    let shallow;
    const mockRouter = jest.mock('next/router', ()=>({
        useRouter: jest.fn().mockImplementation()
    }))



    beforeAll(() => {
        shallow = createShallow();
    })

    it("Render serverId", () => {
        // Wrap Index with mock router to run snapshot tests
        // @ts-ignore
        const wrapper = shallow(<RouterContext.Provider value={mockRouter}><Index /></RouterContext.Provider>);
        expect(wrapper).toMatchSnapshot();
    })

})