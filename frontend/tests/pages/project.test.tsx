import {createShallow} from "@material-ui/core/test-utils";
import React from 'react';
import {render} from '@testing-library/react';
import Index from '../../pages/project/[serverId]';
import {RouterContext} from "next/dist/next-server/lib/router-context";

describe("Project Folder", () =>{
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    const mockUseEffect = jest.spyOn(React, 'useEffect')
    //@ts-ignore
    let router;

    beforeAll(() => {
      router =  useRouter.mockImplementation(() => ({
            query: { serverId: 'TestId' },
        }))
    })

    it("Render serverId", () => {
        // Wrap Index with mock router to run snapshot tests
        // @ts-ignore
        const { container } = render(
            <Index />
        )
        expect(container).toMatchSnapshot();

    })
    it("Test useEffect", ()=>{
        //@ts-ignore
        render(<Index />);
        expect(mockUseEffect).toBeCalled();
    })

})