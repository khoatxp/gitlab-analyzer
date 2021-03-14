import React, {ReactNode} from 'react';
import MenuLayout from '../../components/layout/menu/MenuLayout';
import {mount} from "enzyme";




describe("MenuLayout", () =>{
    // Dummy child to render CardLayout
    const children: ReactNode = <div/>;
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');

    it("Snapshot MenuLayout", async() => {
        useRouter.mockImplementation(() => ({
            query: { projectId: 'TestId', startDateTime: 'TestStartTime', endDateTime: 'TestEndTime' },
        }));
        const rend = mount(
            <MenuLayout tabSelected={0} children={children}/>
        )
        await Promise.resolve();
        expect(rend).toMatchSnapshot();
    });

})